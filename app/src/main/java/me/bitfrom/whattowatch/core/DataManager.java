package me.bitfrom.whattowatch.core;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.core.model.InTheaterPojo;
import me.bitfrom.whattowatch.core.model.MoviePojo;
import me.bitfrom.whattowatch.core.rest.FilmsAPI;
import me.bitfrom.whattowatch.core.storage.DbHelper;
import me.bitfrom.whattowatch.core.storage.PreferencesHelper;
import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observable;

@Singleton
public class DataManager {

    private final FilmsAPI filmsAPI;
    private final DbHelper dbHelper;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(@NonNull FilmsAPI filmsAPI,
                       @NonNull DbHelper dbHelper,
                       @NonNull PreferencesHelper preferencesHelper) {
        this.filmsAPI = filmsAPI;
        this.dbHelper = dbHelper;
        this.preferencesHelper = preferencesHelper;
    }

    @NonNull
    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    @NonNull
    public Observable<List<MoviePojo>> loadTopFilms() {
        return filmsAPI.getTopFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_TOP_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(film -> {
                    List<MoviePojo> result = film.getData().getMovies();
                    Collections.shuffle(result);
                    return  dbHelper.insertFilms(result.subList(0,
                            preferencesHelper.getPreferredNumberOfFilms()),
                            ConstantsManager.CATEGORY_TOP);
                });
    }

    @NonNull
    public Observable<List<MoviePojo>> loadBottomFilms() {
        return filmsAPI.getBottomFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_BOTTOM_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(film -> {
                    List<MoviePojo> result = film.getData().getMovies();
                    Collections.shuffle(result);
                    return  dbHelper.insertFilms(result.subList(0,
                            preferencesHelper.getPreferredNumberOfFilms()),
                            ConstantsManager.CATEGORY_BOTTOM);
                });
    }

    @NonNull
    public Observable<List<MoviePojo>> loadInCinemaFilms() {
        return filmsAPI.getInCinemas(BuildConfig.API_TOKEN, ConstantsManager.API_FORMAT,
                ConstantsManager.TEST_LAN)
                .concatMap(theaterPojo -> {
                    List<MoviePojo> result = new ArrayList<>();
                    int inTheatreSize = theaterPojo.getData().getInTheaters().size();
                    InTheaterPojo inTheaterPojo;
                    for (int i = 0; i < inTheatreSize; i++) {
                        inTheaterPojo = theaterPojo.getData().getInTheaters().get(i);
                        result.addAll(Stream.of(inTheaterPojo.getMovies()).collect(Collectors.toList()));
                    }
                    Collections.shuffle(result);
                    return dbHelper.insertFilms(result, ConstantsManager.CATEGORY_IN_CINEMAS);
                });
    }

    @NonNull
    public Observable<List<MoviePojo>> loadComingSoonFilms() {
        return filmsAPI.getComingSoon(BuildConfig.API_TOKEN, ConstantsManager.API_FORMAT)
                .concatMap(theaterPojo -> {
                    List<MoviePojo> result = new ArrayList<>();
                    int inTheatreSize = theaterPojo.getData().getComingSoon().size();
                    List<MoviePojo> allMovies;
                    for (int i = 0; i < inTheatreSize; i++) {
                        allMovies = theaterPojo.getData().getComingSoon().get(i).getMovies();
                        result.addAll(Stream.of(allMovies).collect(Collectors.toList()));
                        allMovies.clear();
                    }
                    Collections.shuffle(result);
                    return dbHelper.insertFilms(result, ConstantsManager.CATEGORY_COMING_SOON);
                });
    }

    @NonNull
    public Observable<List<FilmEntity>> getTopFilms() {
        return dbHelper.selectFilmsByCategoryId(ConstantsManager.CATEGORY_TOP);
    }

    @NonNull
    public Observable<FilmEntity> getFilmById(@NonNull String filmId) {
        return dbHelper.selectFilmById(filmId).first();
    }

    @NonNull
    public Observable<List<FilmEntity>> getFavoriteFilms() {
        return dbHelper.selectFavoriteFilms().distinct();
    }

    public void addToFavorite(@NonNull String filmId) {
        dbHelper.updateFavorite(filmId, ConstantsManager.FAVORITE);
    }

    public void removeFromFavorite(@NonNull String filmId) {
        dbHelper.updateFavorite(filmId, ConstantsManager.NOT_FAVORITE);
    }

    @NonNull
    public Observable<List<FilmEntity>> getBottomFilms() {
        return dbHelper.selectFilmsByCategoryId(ConstantsManager.CATEGORY_BOTTOM);
    }

    @NonNull
    public Observable<List<FilmEntity>> getInCinemasFilms() {
        return dbHelper.selectFilmsByCategoryId(ConstantsManager.CATEGORY_IN_CINEMAS);
    }

    @NonNull
    public Observable<List<FilmEntity>> getComingSoonFilms() {
        return dbHelper.selectFilmsByCategoryId(ConstantsManager.CATEGORY_COMING_SOON);
    }

    @NonNull
    public Observable<List<FilmEntity>> getSearchResult(@NonNull String title) {
        return dbHelper.searchInFavorites(title).distinct();
    }
}