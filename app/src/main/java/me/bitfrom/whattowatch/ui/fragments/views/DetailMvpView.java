package me.bitfrom.whattowatch.ui.fragments.views;

import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showFilmInfo(Film film);

    void showUnknownError();

    void showAddedToFavorites();

    void showRemovedFromFavorites();

    void shareWithFriends(String shareInfo);
}
