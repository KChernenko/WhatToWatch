package me.bitfrom.whattowatch.ui.fragments;


import java.util.List;

import javax.inject.Inject;

import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RandomFilmsPresenter extends BasePresenter<RandomFilmsMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public RandomFilmsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(RandomFilmsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadFims() {
        checkViewAttached();
        mSubscription = mDataManager.getFilms()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new Observer<List<FilmModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<FilmModel> films) {
                        if (films.isEmpty()) {
                            getMvpView().showFilmsListEmpty();
                        } else {
                            getMvpView().showFilmsList(films);
                        }
                    }
                });
    }
}
