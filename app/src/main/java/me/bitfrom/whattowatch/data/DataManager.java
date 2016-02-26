package me.bitfrom.whattowatch.data;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.data.model.Film;
import me.bitfrom.whattowatch.data.model.MoviePojo;
import me.bitfrom.whattowatch.data.rest.FilmsAPI;
import me.bitfrom.whattowatch.data.storage.DBHelper;
import me.bitfrom.whattowatch.data.storage.PreferencesHelper;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observable;

@Singleton
public class DataManager {

    private final FilmsAPI mFilmsAPI;
    private final DBHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(FilmsAPI filmsAPI, DBHelper dbHelper, PreferencesHelper preferencesHelper) {
        mFilmsAPI = filmsAPI;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<MoviePojo> loadTopFilms() {
        return mFilmsAPI.getTopFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_TOP_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(film -> {
                    List<MoviePojo> result = film.getData().getMovies();
                    Collections.shuffle(result);
                    return mDbHelper.setTopFilms(result.subList(0,
                            mPreferencesHelper.getPrefferedNuberOfFilms()));
                });
    }

    public Observable<List<Film>> getTopFilms() {
        return mDbHelper.getTopFilms().distinct();
    }

    public Observable<Film> getFilmById(String filmId) {
        return mDbHelper.getFilmById(filmId).first();
    }

    public Observable<List<Film>> getFavoriteFilms() {
        return mDbHelper.getFavoriteFilms().distinct();
    }

    public void addToFavorite(String filmId) {
        mDbHelper.updateFavorite(filmId, ConstantsManager.FAVORITE);
    }

    public void removeFromFavorite(String filmId) {
        mDbHelper.updateFavorite(filmId, ConstantsManager.NOT_FAVORITE);
    }

    public Observable<MoviePojo> loadBottomFilms() {
        return mFilmsAPI.getBottomFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_BOTTOM_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(film -> {
                    List<MoviePojo> result = film.getData().getMovies();
                    Collections.shuffle(result);
                    return mDbHelper.setBottomFilms(result.subList(0,
                            mPreferencesHelper.getPrefferedNuberOfFilms()));
                });
    }

    public Observable<List<Film>> getBottomFilms() {
        return mDbHelper.getBottomFilms().distinct();
    }
}
