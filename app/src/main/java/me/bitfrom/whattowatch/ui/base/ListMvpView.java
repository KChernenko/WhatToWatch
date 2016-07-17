package me.bitfrom.whattowatch.ui.base;

import android.support.annotation.NonNull;

import java.util.List;

import me.bitfrom.whattowatch.core.model.Film;

public interface ListMvpView extends MvpViewWithInternet {

    void showFilmsList(@NonNull List<Film> films);

    void showInternetUnavailableError();

    void loadNewFilms(boolean pullToRefresh);

    void showLoading(boolean pullToRefresh);

    void showListIsEmpty();
}