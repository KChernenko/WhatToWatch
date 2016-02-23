package me.bitfrom.whattowatch.ui.fragments.views;

import android.support.v7.app.AlertDialog;

import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showFilmInfo(FilmModel film);

    void showUnknownError();

    void showAddedToFavorites();

    void showRemovedFromFavorites();

    void shareWithFriends(String shareInfo);

    void imdbLinkDialog(AlertDialog.Builder alertDialog);
}
