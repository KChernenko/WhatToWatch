package me.bitfrom.whattowatch.core.storage;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.core.image.ImageDownloader;
import me.bitfrom.whattowatch.core.model.DirectorModel;
import me.bitfrom.whattowatch.core.model.MovieModel;
import me.bitfrom.whattowatch.core.model.WriterModel;
import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static me.bitfrom.whattowatch.core.image.ImageLoaderInteractor.Flag;

@Singleton
public class DbHelper {

    private final BriteDatabase database;
    private ImageDownloader imageDownloader;

    @Inject
    public DbHelper(@NonNull DbOpenHelper dbOpenHelper,
                    @NonNull ImageDownloader imageDownloader) {
        database = new SqlBrite.Builder().build().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
        this.imageDownloader = imageDownloader;
    }

    @NonNull @SuppressWarnings("unchecked")
    public Observable<List<MovieModel>> insertFilms(@NonNull final List<MovieModel> newMovies, int categoryId) {
        return Observable.defer(() -> {
            BriteDatabase.Transaction transaction = database.newTransaction();
            MovieModel movie = null;

            try {
                database.delete(FilmEntity.TABLE_NAME,
                        FilmEntity.INFAVORITE + " = ?" + " AND " + FilmEntity.CATEGORYID + " = ?",
                        String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(categoryId));

                for (int i = 0; i < newMovies.size(); i++) {
                    movie = newMovies.get(i);

                    long result = database.insert(FilmEntity.TABLE_NAME,
                            FilmEntity.FACTORY.marshal()
                                .imdbId(movie.getIdIMDB())
                                .imbdUrl(movie.getUrlIMDB())
                                .countries(TextUtils.join(ConstantsManager.ARRAY_DIVIDER, movie.getCountries()))
                                .directors(TextUtils.join(ConstantsManager.ARRAY_DIVIDER,
                                        Stream.of(movie.getDirectors()).map(DirectorModel::getName).collect(Collectors.toList())))
                                .writers(TextUtils.join(ConstantsManager.ARRAY_DIVIDER,
                                        Stream.of(movie.getWriters()).map(WriterModel::getName).collect(Collectors.toList())))
                                .genres(TextUtils.join(ConstantsManager.ARRAY_DIVIDER, movie.getGenres()))
                                .plot(!TextUtils.isEmpty(movie.getPlot()) ? movie.getPlot() : "N/A")
                                .rating(!TextUtils.isEmpty(movie.getRating()) ? movie.getRating() : "N/A")
                                .runtime(!TextUtils.isEmpty(movie.getRuntime()) ? movie.getRuntime() : "N/A")
                                .title(movie.getTitle())
                                .posterUrl(movie.getUrlPoster())
                                .year(movie.getYear())
                                .inFavorite(ConstantsManager.NOT_FAVORITE)
                                .categoryId(categoryId)
                            .asContentValues(), SQLiteDatabase.CONFLICT_REPLACE);

                    cacheImage(movie.getUrlPoster());
                    if (result < 0) Timber.e("Failed to insert data: %s", String.valueOf(result));
                    //If user decided to refresh items while syncing in progress
                    transaction.yieldIfContendedSafely();
                }
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }

            return Observable.just(newMovies);
        });
    }

    @NonNull
    public Observable<List<FilmEntity>> selectFilmsByCategoryId(int categoryId) {
        return database.createQuery(FilmEntity.TABLE_NAME, FilmEntity.SELECT_BY_CATEGORY_ID,
                String.valueOf(categoryId))
                .mapToList(FilmEntity.MAPPER_SELECT_BY_CATEGORY_ID::map);
    }

    @NonNull
    public Observable<FilmEntity> selectFilmById(@NonNull final String filmId) {
        return database.createQuery(FilmEntity.TABLE_NAME, FilmEntity.SELECT_BY_ID,
                filmId).mapToOne(FilmEntity.MAPPER_SELECT_BY_ID::map);
    }

    @NonNull
    public Observable<List<FilmEntity>> selectFavoriteFilms() {
        return database.createQuery(FilmEntity.TABLE_NAME, FilmEntity.SELECT_FAVORITES)
                .mapToList(FilmEntity.MAPPER_SELECT_FAVORITES::map);
    }

    public void updateFavorite(String filmId, int favorite) {
        BriteDatabase.Transaction transaction = database.newTransaction();
        switch (favorite) {
            case ConstantsManager.FAVORITE:
                try {
                    int result = database.update(FilmEntity.TABLE_NAME,
                            FilmEntity.FACTORY.marshal()
                            .inFavorite(ConstantsManager.FAVORITE)
                            .asContentValues(), FilmEntity.IMDBID + " = ?", filmId);
                    if (result <= 0) Timber.d("Film wasn't added to favorites: %s", String.valueOf(filmId));
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
                break;
            case ConstantsManager.NOT_FAVORITE:
                try {
                    int result = database.update(FilmEntity.TABLE_NAME,
                            FilmEntity.FACTORY.marshal()
                            .inFavorite(ConstantsManager.NOT_FAVORITE)
                            .asContentValues(), FilmEntity.IMDBID + " = ?", filmId);
                    if (result <= 0) Timber.d("Film wasn't removed from favorites: %s", String.valueOf(filmId));
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                }
                break;
            default:
                transaction.close();
                break;
        }
    }

    @NonNull
    public Observable<List<FilmEntity>> searchInFavorites(@NonNull String title) {
        return database.createQuery(FilmEntity.TABLE_NAME, FilmEntity.SELECT_LIKE,
                new String[]{"%" + title + "%"})
                .mapToList(FilmEntity.MAPPER_SELECT_LIKE::map);
    }

    private void cacheImage(String imgUrl) {
        imageDownloader.loadImage(Flag.LOAD_ONLY, imgUrl, null);
    }
}