package me.bitfrom.whattowatch.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    public Observable<Observable<Movie>> loadFilms() {
        return mFilmsAPI.getFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_LIST_END, ConstantsManager.API_TOKEN,
                ConstantsManager.API_FORMAT, ConstantsManager.API_DATA)
                .map(new Func1<Film, Observable<Movie>>() {
                    @Override
                    public Observable<Movie> call(Film film) {
                        return mDbHelper.setFilms(film.getData().getMovies());
                    }
                });
    }

    public Observable<List<FilmModel>> getFilms() {
        return mDbHelper.getFilms().distinct();
    }
}
