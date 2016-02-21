package me.bitfrom.whattowatch.ui.fragments.views;

import java.util.List;

import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.base.MvpView;


public interface RandomFilmsMvpView extends MvpView {

    void showFilmsList(List<FilmModel> films);

    void showUnknownError();

    void showInternetUnavailableError();

    void loadNewFilms(boolean pullToRefresh);

    void showLoading(boolean pullToRefresh);
}
