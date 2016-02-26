package me.bitfrom.whattowatch.data;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.data.model.Film;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.data.model.Movie;
import me.bitfrom.whattowatch.data.rest.FilmsAPI;
import me.bitfrom.whattowatch.data.storage.DBHelper;
import me.bitfrom.whattowatch.data.storage.PreferencesHelper;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observable;
import rx.functions.Func1;

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

    public Observable<Movie> loadFilms() {
        return mFilmsAPI.getFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_LIST_END, BuildConfig.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .concatMap(new Func1<Film, Observable<Movie>>() {
                    @Override
                    public Observable<Movie> call(Film film) {
                        List<Movie> result = film.getData().getMovies();
                        Collections.shuffle(result);
                        return mDbHelper.setFilms(result.subList(0,
                                mPreferencesHelper.getPrefferedNuberOfFilms()));
                    }
                });
    }

    public Observable<List<FilmModel>> getFilms() {
        return mDbHelper.getFilms().distinct();
    }

    public Observable<FilmModel> getTopFilmById(String filmId) {
        return mDbHelper.getTopFilmById(filmId).first();
    }

    public Observable<List<FilmModel>> getFavoriteFilms() {
        return mDbHelper.getFavoriteFilms().distinct();
    }

    public void addToFavorite(String filmId) {
        mDbHelper.updateFavorite(filmId, ConstantsManager.FAVORITE);
    }

    public void removeFromFavorite(String filmId) {
        mDbHelper.updateFavorite(filmId, ConstantsManager.NOT_FAVORITE);
    }
}
