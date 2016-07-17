package me.bitfrom.whattowatch.core;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.core.model.InTheaterPojo;
import me.bitfrom.whattowatch.core.model.MoviePojo;
import me.bitfrom.whattowatch.core.rest.FilmsAPI;
import me.bitfrom.whattowatch.core.storage.DBHelper;
import me.bitfrom.whattowatch.core.storage.PreferencesHelper;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observable;

@Singleton
public class DataManager {

    private final FilmsAPI filmsAPI;
    private final DBHelper dbHelper;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(@NonNull FilmsAPI filmsAPI,
                       @NonNull DBHelper dbHelper,
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
    public Observable<MoviePojo> loadTopFilms() {
        return filmsAPI.getTopFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_TOP_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(film -> {
                    List<MoviePojo> result = film.getData().getMovies();
                    Collections.shuffle(result);
                    return dbHelper.setTopFilms(result.subList(0,
                            preferencesHelper.getProfferedNuderOfFilms()));
                }).onBackpressureBuffer();
    }

    @NonNull
    public Observable<List<Film>> getTopFilms() {
        return dbHelper.getTopFilms();
    }

    @NonNull
    public Observable<Film> getFilmById(@NonNull String filmId) {
        return dbHelper.getFilmById(filmId).first();
    }

    @NonNull
    public Observable<List<Film>> getFavoriteFilms() {
        return dbHelper.getFavoriteFilms().distinct();
    }

    public void addToFavorite(@NonNull String filmId) {
        dbHelper.updateFavorite(filmId, ConstantsManager.FAVORITE);
    }

    public void removeFromFavorite(@NonNull String filmId) {
        dbHelper.updateFavorite(filmId, ConstantsManager.NOT_FAVORITE);
    }

    @NonNull
    public Observable<MoviePojo> loadBottomFilms() {
        return filmsAPI.getBottomFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_BOTTOM_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(film -> {
                    List<MoviePojo> result = film.getData().getMovies();
                    Collections.shuffle(result);
                    return dbHelper.setBottomFilms(result.subList(0,
                            preferencesHelper.getProfferedNuderOfFilms()));
                }).onBackpressureBuffer();
    }

    @NonNull
    public Observable<List<Film>> getBottomFilms() {
        return dbHelper.getBottomFilms();
    }

    @NonNull
    public Observable<MoviePojo> loadInCinemaFilms() {
        return filmsAPI.getInCinemas(BuildConfig.API_TOKEN, ConstantsManager.API_FORMAT,
                ConstantsManager.TEST_LAN)
                .concatMap(theaterPojo -> {
                    List<MoviePojo> result = new ArrayList<>();
                    int inTheatreSize = theaterPojo.getData().getInTheaters().size();
                    InTheaterPojo inTheaterPojo;
                    for (int i = 0; i < inTheatreSize; i++) {
                        inTheaterPojo = theaterPojo.getData().getInTheaters().get(i);
                        for (MoviePojo moviePojo : inTheaterPojo.getMovies()) {
                            result.add(moviePojo);
                        }
                    }
                    Collections.shuffle(result);
                    return dbHelper.setInCinemas(result);
                }).onBackpressureBuffer();
    }

    @NonNull
    public Observable<List<Film>> getInCinemasFilms() {
        return dbHelper.getInCinemasFilms();
    }

    @NonNull
    public Observable<MoviePojo> loadComingSoonFilms() {
        return filmsAPI.getComingSoon(BuildConfig.API_TOKEN, ConstantsManager.API_FORMAT)
                .concatMap(theaterPojo -> {
                    List<MoviePojo> result = new ArrayList<>();
                    int inTheatreSize = theaterPojo.getData().getComingSoon().size();
                    List<MoviePojo> allMovies;
                    for (int i = 0; i < inTheatreSize; i++) {
                        allMovies = theaterPojo.getData().getComingSoon().get(i).getMovies();
                        for (MoviePojo moviePojo : allMovies) {
                            result.add(moviePojo);
                        }
                        allMovies.clear();
                    }
                    Collections.shuffle(result);
                    return dbHelper.setComingSoon(result);
                }).onBackpressureBuffer();
    }

    @NonNull
    public Observable<List<Film>> getComingSoonFilms() {
        return dbHelper.getComingSoon().distinct();
    }

    @NonNull
    public Observable<List<Film>> getSearchResult(@NonNull String title) {
        return dbHelper.searchInFavorite(title).distinct();
    }
}