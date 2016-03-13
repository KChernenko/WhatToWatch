package me.bitfrom.whattowatch.ui.base;

import java.util.List;

import me.bitfrom.whattowatch.core.model.Film;

public interface ListMvpView extends MvpView {

    void showFilmsList(List<Film> films);

    void showUnknownError();

    void showInternetUnavailableError();

    void loadNewFilms(boolean pullToRefresh);

    void showLoading(boolean pullToRefresh);

    void showListIsEmpty();
}
