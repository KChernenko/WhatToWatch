package me.bitfrom.whattowatch.ui.fragments.views;

import android.support.annotation.NonNull;

import java.util.List;

import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.ui.base.MvpView;

public interface FavoritesMvpView extends MvpView {

    void showListOfFavorites(@NonNull List<Film> favoriteFilms);

    void showUnknownError();

    void showNothingHasFound();

    void showListIsEmpty();

}
