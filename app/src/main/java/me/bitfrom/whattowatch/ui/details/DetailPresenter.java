package me.bitfrom.whattowatch.ui.details;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager dataManager;

    private Subscription subscription;

    @Inject
    public DetailPresenter(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(@NonNull DetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    public void getFilm(@NonNull final String filmId) {
        checkViewAttached();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = dataManager.getFilmById(filmId)
                .replay().autoConnect()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(film -> {
                    if (film == null) {
                        getMvpView().showUnknownError();
                    } else {
                        getMvpView().showFilmInfo(film);
                    }
                }, throwable -> {
                    getMvpView().showUnknownError();
                    Timber.e(throwable, "Error occurred while retrieving film's info!");
                });
    }

    public void updateFavorites(@NonNull final String filmId) {
        checkViewAttached();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = dataManager.getFilmById(filmId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(film -> {
                    if (film.inFavorite() == ConstantsManager.NOT_FAVORITE) {
                        dataManager.addToFavorite(filmId);
                        getMvpView().showAddedToFavorites();
                    } else {
                        dataManager.removeFromFavorite(filmId);
                        getMvpView().showRemovedFromFavorites();
                    }
                });
    }
}