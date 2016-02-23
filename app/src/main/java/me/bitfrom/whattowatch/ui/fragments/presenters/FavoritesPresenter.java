package me.bitfrom.whattowatch.ui.fragments.presenters;


import java.util.List;

import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.ui.fragments.views.FavoritesMvpView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void getFavoriteFilms() {
        checkViewAttached();
        mSubscription = mDataManager.getFavoriteFilms()
                .subscribeOn(Schedulers.newThread())
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FilmModel>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Query completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showUnknownError();
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(List<FilmModel> films) {
                        if (films.isEmpty()) {
                            getMvpView().showEmptyList();
                        } else {
                            getMvpView().showListOfFavorites(films);
                        }
                    }
                });
    }
}
