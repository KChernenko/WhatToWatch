package me.bitfrom.whattowatch.ui.fragments.views;

import java.util.List;

import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.ui.base.MvpView;

public interface FavoritesMvpView extends MvpView {

    void showListOfFavorites(List<Film> favoriteFilms);

    void showEmptyList();

    void showUnknownError();

    void showNothingHasFound();

}
