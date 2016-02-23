package me.bitfrom.whattowatch.ui.fragments.views;


import java.util.List;

import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.base.MvpView;

public interface FavoritesMvpView extends MvpView {

    void showListOfFavorites(List<FilmModel> favoriteFilms);

    void showEmptyList();

    void showUnknownError();

}
