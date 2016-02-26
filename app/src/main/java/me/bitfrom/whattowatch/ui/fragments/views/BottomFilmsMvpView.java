package me.bitfrom.whattowatch.ui.fragments.views;


import java.util.List;

import me.bitfrom.whattowatch.data.model.Film;
import me.bitfrom.whattowatch.ui.activity.MainMvpView;

public interface BottomFilmsMvpView extends MainMvpView {

    void showFilmsList(List<Film> films);

    void showUnknownError();

    void showInternetUnavailableError();

    void loadNewFilms(boolean pullToRefresh);

    void showLoading(boolean pullToRefresh);
}
