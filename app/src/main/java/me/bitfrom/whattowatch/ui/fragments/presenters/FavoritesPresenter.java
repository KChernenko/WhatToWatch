package me.bitfrom.whattowatch.ui.fragments.presenters;


import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.ui.fragments.views.FavoritesMvpView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FavoritesPresenter extends BasePresenter<FavoritesMvpView>{

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public FavoritesPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(FavoritesMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
    }

    public void getFavoriteFilms() {
        checkViewAttached();
        mSubscription = mDataManager.getFavoriteFilms()
                .subscribeOn(Schedulers.io())
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(films -> {
                    if (films.isEmpty()) {
                        getMvpView().showEmptyList();
                    } else {
                        getMvpView().showListOfFavorites(films);
                    }
                }, throwable -> {
                    getMvpView().showUnknownError();
                    if (BuildConfig.DEBUG) {
                        throwable.printStackTrace();
                    }
                });
    }
}
