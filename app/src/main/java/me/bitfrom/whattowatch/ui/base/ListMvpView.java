package me.bitfrom.whattowatch.ui.base;

import android.support.annotation.NonNull;

import java.util.List;

import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;

public interface ListMvpView extends MvpViewWithInternet {

    void showFilmsList(@NonNull List<FilmEntity> films);

    void showInternetUnavailableError();

    void loadNewFilms();

    void showLoading(boolean pullToRefresh);

    void showListIsEmpty();
}