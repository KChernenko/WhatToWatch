package me.bitfrom.whattowatch.ui.favorites;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class FavoritesPresenter extends BasePresenter<FavoritesMvpView> {

    private final DataManager dataManager;

    private Subscription subscription;

    @Inject
    public FavoritesPresenter(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(@NonNull FavoritesMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    public void getFavoriteFilms() {
        checkViewAttached();
        subscription = dataManager.getFavoriteFilms()
                .subscribeOn(Schedulers.io())
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(films -> {
                    if (films.isEmpty()) {
                        getMvpView().showListIsEmpty();
                    } else {
                        getMvpView().showListOfFavorites(films);
                    }
                }, throwable -> {
                    getMvpView().showUnknownError();
                    Timber.e(throwable, "Error occurred while retrieving list of favorites");
                });
    }

    public void search(String title) {
        checkViewAttached();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = dataManager.getSearchResult(title)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(films -> {
                    if (films.isEmpty()) {
                        getMvpView().showNothingHasFound();
                    } else {
                        getMvpView().showListOfFavorites(films);
                    }
                }, throwable -> {
                    getMvpView().showUnknownError();
                    Timber.e(throwable, "Error occurred!");
                });
    }
}
