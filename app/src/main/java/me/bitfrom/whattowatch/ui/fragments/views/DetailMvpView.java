package me.bitfrom.whattowatch.ui.fragments.views;

import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showFilmInfo(FilmModel film);

    void showUnknownError();

    void addToFavorites();

    void shareWithFriends();

    void openImdbLink();
}
