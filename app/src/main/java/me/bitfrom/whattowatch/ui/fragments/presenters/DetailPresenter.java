package me.bitfrom.whattowatch.ui.fragments.presenters;


import android.content.Context;

import javax.inject.Inject;

import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.injection.ActivityContext;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.ui.fragments.views.DetailMvpView;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager mDataManager;
    private Context mContext;
    private Subscription mSubscription;

    @Inject
    public DetailPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(DetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        mContext = null;
    }

    public void getFilm(String filmId) {
        checkViewAttached();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.getTopFilmById(filmId)
                .subscribeOn(Schedulers.newThread())
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilmModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showUnknownError();
                        Timber.d("Error occurred!", e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(FilmModel film) {
                        if (film == null) {
                            getMvpView().showUnknownError();
                        } else {
                            getMvpView().showFilmInfo(film);
                        }
                    }
                });
    }

    public void updateFavorites(final String filmId) {
        checkViewAttached();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.getTopFilmById(filmId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FilmModel>() {
                    @Override
                    public void call(FilmModel film) {
                        if (film.favorite.equals(String.valueOf(ConstantsManager.NOT_FAVORITE))) {
                            mDataManager.addToFavorite(filmId);
                            getMvpView().showAddedToFavorites();
                        } else {
                            mDataManager.removeFromFavorite(filmId);
                            getMvpView().showRemovedFromFavorites();
                        }
                    }
                });
    }
}
