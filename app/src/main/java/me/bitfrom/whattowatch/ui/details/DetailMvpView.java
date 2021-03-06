package me.bitfrom.whattowatch.ui.details;

import android.support.annotation.NonNull;

import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showFilmInfo(@NonNull FilmEntity film);

    void showUnknownError();

    void showAddedToFavorites();

    void showRemovedFromFavorites();

}