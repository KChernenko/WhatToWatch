package me.bitfrom.whattowatch.core.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.core.image.ImageDownloader;
import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.core.model.MoviePojo;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static me.bitfrom.whattowatch.core.image.ImageLoaderInteractor.Flag;

@Singleton
public class DBHelper {

    private final BriteDatabase database;
    private ImageDownloader imageDownloader;

    @Inject
    public DBHelper(@NonNull DBOpenHelper dbOpenHelper,
                    @NonNull ImageDownloader imageDownloader) {
        database = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
        this.imageDownloader = imageDownloader;
    }

    @NonNull
    public BriteDatabase getBriteDb() {
        return database;
    }

    @NonNull
    public Observable<MoviePojo> setTopFilms(@NonNull final List<MoviePojo> newMovies) {
        return Observable.defer(() -> {
            BriteDatabase.Transaction transaction = database.newTransaction();
            MoviePojo movie = null;

            try {
                database.delete(DBContract.FilmsTable.TABLE_NAME,
                        DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                                " AND " + DBContract.FilmsTable.COLUMN_CATEGORY + " = ?",
                        String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(ConstantsManager.TOP));

                for (int i = 0; i < newMovies.size(); i++) {
                    movie = newMovies.get(i);

                    long result = database.insert(DBContract.FilmsTable.TABLE_NAME,
                            DBContract.FilmsTable.toContentValues(movie,
                                    ConstantsManager.TOP),
                            SQLiteDatabase.CONFLICT_REPLACE);

                    cacheImage(movie.getUrlPoster());
                    if (result < 0) Timber.e(ConstantsManager.DB_LOG_TAG, "Failed to insert data: " + result);
                }
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }

            return Observable.just(movie);
        });
    }

    @NonNull
    public Observable<List<Film>> getTopFilms() {
        return database.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME +
                        " WHERE " + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                        " AND " + DBContract.FilmsTable.COLUMN_CATEGORY + " = ?",
                new String[] {String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(ConstantsManager.TOP)})
                .mapToList(DBContract.FilmsTable::parseCursor);
    }

    @NonNull
    public Observable<Film> getFilmById(@NonNull String filmId) {
        return database.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME + " WHERE "
                        + DBContract.FilmsTable.COLUMN_IMDB_ID + " = ?", filmId)
                .map(query -> {
                    Cursor cursor = query.run();
                    Film result = null;
                    if (cursor != null) {
                        try {
                            cursor.moveToFirst();
                            result = DBContract.FilmsTable.parseCursor(cursor);
                        } finally {
                            cursor.close();
                        }
                    }
                    return result;
                });
    }

    @NonNull
    public Observable<List<Film>> getFavoriteFilms() {
        return database.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME + " WHERE "
                        + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?",
                String.valueOf(ConstantsManager.FAVORITE))
                .mapToList(DBContract.FilmsTable::parseCursor);
    }

    public void updateFavorite(String filmId, int favorite) {
        ContentValues values = new ContentValues(1);
        BriteDatabase.Transaction transaction = database.newTransaction();
        switch (favorite) {
            case ConstantsManager.FAVORITE:
                values.put(DBContract.FilmsTable.COLUMN_FAVORITE, ConstantsManager.FAVORITE);
                try {
                    database.update(DBContract.FilmsTable.TABLE_NAME,
                            values, SQLiteDatabase.CONFLICT_REPLACE,
                            DBContract.FilmsTable.COLUMN_IMDB_ID + " = ?",
                            filmId);
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                    values.clear();
                }
                break;
            case ConstantsManager.NOT_FAVORITE:
                values.put(DBContract.FilmsTable.COLUMN_FAVORITE, ConstantsManager.NOT_FAVORITE);
                try {
                    database.update(DBContract.FilmsTable.TABLE_NAME,
                            values, SQLiteDatabase.CONFLICT_REPLACE,
                            DBContract.FilmsTable.COLUMN_IMDB_ID + " = ?",
                            filmId);
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                    values.clear();
                }
                break;
            default:
                transaction.close();
                break;
        }
    }

    @NonNull
    public Observable<MoviePojo> setBottomFilms(@NonNull final List<MoviePojo> newMovies) {
        return Observable.defer(() -> {
            BriteDatabase.Transaction transaction = database.newTransaction();
            MoviePojo movie = null;
            try {
                database.delete(DBContract.FilmsTable.TABLE_NAME,
                        DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                                " AND " + DBContract.FilmsTable.COLUMN_CATEGORY + " = ?",
                        String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(ConstantsManager.BOTTOM));

                for (int i = 0; i < newMovies.size(); i++) {
                    movie = newMovies.get(i);
                    long result = database.insert(DBContract.FilmsTable.TABLE_NAME,
                            DBContract.FilmsTable.toContentValues(movie,
                                    ConstantsManager.BOTTOM),
                            SQLiteDatabase.CONFLICT_REPLACE);

                    cacheImage(movie.getUrlPoster());
                    if (result < 0) Timber.e(ConstantsManager.DB_LOG_TAG, "Failed to insert data: " + result);
                }
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }

            return Observable.just(movie);
        });
    }

    @NonNull
    public Observable<List<Film>>

    getBottomFilms() {
        return database.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME +
                        " WHERE " + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                        " AND " + DBContract.FilmsTable.COLUMN_CATEGORY + " = ?",
                new String[] {String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(ConstantsManager.BOTTOM)})
                .mapToList(DBContract.FilmsTable::parseCursor);
    }

    @NonNull
    public Observable<MoviePojo> setInCinemas(@NonNull final List<MoviePojo> movies) {
        return Observable.defer(() -> {
            BriteDatabase.Transaction transaction = database.newTransaction();
            MoviePojo movie = null;

            try {
                database.delete(DBContract.FilmsTable.TABLE_NAME,
                        DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                                " AND " + DBContract.FilmsTable.COLUMN_CATEGORY + " = ?",
                        String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(ConstantsManager.IN_CINEMAS));

                for (int i = 0; i < movies.size(); i++) {
                    movie = movies.get(i);

                    long result = database.insert(DBContract.FilmsTable.TABLE_NAME,
                            DBContract.FilmsTable.toContentValues(movie,
                                    ConstantsManager.IN_CINEMAS),
                            SQLiteDatabase.CONFLICT_REPLACE);

                    cacheImage(movie.getUrlPoster());
                    if (result < 0) Timber.e(ConstantsManager.DB_LOG_TAG, "Failed to insert data: " + result);
                }
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }

            return Observable.just(movie);
        });
    }

    @NonNull
    public Observable<List<Film>> getInCinemasFilms() {
        return database.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME +
                        " WHERE " + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                        " AND " + DBContract.FilmsTable.COLUMN_CATEGORY + " = ?",
                new String[] {String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(ConstantsManager.IN_CINEMAS)})
                .mapToList(DBContract.FilmsTable::parseCursor);
    }

    public Observable<MoviePojo> setComingSoon(@NonNull final List<MoviePojo> movies) {
        return Observable.defer(() -> {
            BriteDatabase.Transaction transaction = database.newTransaction();
            MoviePojo movie = null;

            try {
                database.delete(DBContract.FilmsTable.TABLE_NAME,
                        DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                                " AND " + DBContract.FilmsTable.COLUMN_CATEGORY + " = ?",
                        String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(ConstantsManager.COMING_SOON));

                for (int i = 0; i < movies.size(); i++) {
                    movie = movies.get(i);

                    long result = database.insert(DBContract.FilmsTable.TABLE_NAME,
                            DBContract.FilmsTable.toContentValues(movie,
                                    ConstantsManager.COMING_SOON),
                            SQLiteDatabase.CONFLICT_REPLACE);

                    cacheImage(movie.getUrlPoster());
                    if (result < 0) Timber.e(ConstantsManager.DB_LOG_TAG, "Failed to insert data: " + result);
                }
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }

           return Observable.just(movie);
        });
    }

    @NonNull
    public Observable<List<Film>> getComingSoon() {
        return database.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME +
                        " WHERE " + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                        " AND " + DBContract.FilmsTable.COLUMN_CATEGORY + " = ?",
                new String[] {String.valueOf(ConstantsManager.NOT_FAVORITE),
                        String.valueOf(ConstantsManager.COMING_SOON)})
                .mapToList(DBContract.FilmsTable::parseCursor);
    }

    @NonNull
    public Observable<List<Film>> searchInFavorite(@NonNull String title) {
        return database.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME +
                " WHERE " + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?" +
                " AND " + DBContract.FilmsTable.COLUMN_TITLE + " LIKE ?",
                new String[] {String.valueOf(ConstantsManager.FAVORITE),
                "%" + title + "%"})
                .mapToList(DBContract.FilmsTable::parseCursor)
                .first();
    }

    public Observable<Void> clearTables() {
        return Observable.create(subscriber -> {
            if (subscriber.isUnsubscribed()) return;

            BriteDatabase.Transaction transaction = database.newTransaction();
            try {
                Cursor cursor = database.query("SELECT name FROM sqlite_master WHERE type='table'");
                while (cursor.moveToNext()) {
                    database.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                }
                cursor.close();
                transaction.markSuccessful();
                subscriber.onCompleted();
            } finally {
                transaction.end();
            }
        });
    }

    private void cacheImage(String imgUrl) {
        imageDownloader.loadImage(Flag.LOAD_ONLY, imgUrl, null);
    }
}