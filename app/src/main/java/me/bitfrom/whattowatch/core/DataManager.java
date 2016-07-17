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

    private final FilmsAPI mFilmsAPI;
    private final DBHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(@NonNull FilmsAPI filmsAPI, @NonNull DBHelper dbHelper, @NonNull PreferencesHelper preferencesHelper) {
        mFilmsAPI = filmsAPI;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
    }

    @NonNull
    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    @NonNull
    public Observable<MoviePojo> loadTopFilms() {
        return mFilmsAPI.getTopFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_TOP_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(film -> {
                    List<MoviePojo> result = film.getData().getMovies();
                    Collections.shuffle(result);
                    return mDbHelper.setTopFilms(result.subList(0,
                            mPreferencesHelper.getProfferedNuderOfFilms()));
                }).onBackpressureBuffer();
    }

    @NonNull
    public Observable<List<Film>> getTopFilms() {
        return mDbHelper.getTopFilms();
    }

    @NonNull
    public Observable<Film> getFilmById(@NonNull String filmId) {
        return mDbHelper.getFilmById(filmId).first();
    }

    @NonNull
    public Observable<List<Film>> getFavoriteFilms() {
        return mDbHelper.getFavoriteFilms().distinct();
    }

    public void addToFavorite(@NonNull String filmId) {
        mDbHelper.updateFavorite(filmId, ConstantsManager.FAVORITE);
    }

    public void removeFromFavorite(@NonNull String filmId) {
        mDbHelper.updateFavorite(filmId, ConstantsManager.NOT_FAVORITE);
    }

    @NonNull
    public Observable<MoviePojo> loadBottomFilms() {
        return mFilmsAPI.getBottomFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_BOTTOM_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(film -> {
                    List<MoviePojo> result = film.getData().getMovies();
                    Collections.shuffle(result);
                    return mDbHelper.setBottomFilms(result.subList(0,
                            mPreferencesHelper.getProfferedNuderOfFilms()));
                }).onBackpressureBuffer();
    }

    @NonNull
    public Observable<List<Film>> getBottomFilms() {
        return mDbHelper.getBottomFilms();
    }

    @NonNull
    public Observable<MoviePojo> loadInCinemaFilms() {
        return mFilmsAPI.getInCinemas(BuildConfig.API_TOKEN, ConstantsManager.API_FORMAT,
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
                    return mDbHelper.setInCinemas(result);
                }).onBackpressureBuffer();
    }

    @NonNull
    public Observable<List<Film>> getInCinemasFilms() {
        return mDbHelper.getInCinemasFilms();
    }

    @NonNull
    public Observable<MoviePojo> loadComingSoonFilms() {
        return mFilmsAPI.getComingSoon(BuildConfig.API_TOKEN, ConstantsManager.API_FORMAT)
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
                    return mDbHelper.setComingSoon(result);
                }).onBackpressureBuffer();
    }

    @NonNull
    public Observable<List<Film>> getComingSoonFilms() {
        return mDbHelper.getComingSoon().distinct();
    }

    @NonNull
    public Observable<List<Film>> getSearchResult(@NonNull String title) {
        return mDbHelper.searchInFavorite(title).distinct();
    }
}