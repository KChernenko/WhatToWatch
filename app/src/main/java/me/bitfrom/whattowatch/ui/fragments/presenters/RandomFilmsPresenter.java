package me.bitfrom.whattowatch.ui.fragments.presenters;

import android.content.Context;
import android.content.Intent;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.LoadService;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.data.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.injection.ActivityContext;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.ui.fragments.views.RandomFilmsMvpView;
import me.bitfrom.whattowatch.utils.NetworkStateChecker;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class RandomFilmsPresenter extends BasePresenter<RandomFilmsMvpView> {

    private final DataManager mDataManager;
    private FilmsSyncAdapter mFilmsSyncAdapter;
    private Context mContext;
    private Subscription mSubscription;

    @Inject
    public RandomFilmsPresenter(DataManager dataManager, @ActivityContext Context context,
                                FilmsSyncAdapter filmsSyncAdapter) {
        mDataManager = dataManager;
        mContext = context;
        mFilmsSyncAdapter = filmsSyncAdapter;
    }

    @Override
    public void attachView(RandomFilmsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        mContext = null;
    }

    public void loadFilms(boolean pullToRefresh) {
        checkViewAttached();
        getMvpView().showLoading(pullToRefresh);
        if (NetworkStateChecker.isNetworkAvailable(mContext)) {
            Glide.get(mContext).clearMemory();
            mContext.startService(new Intent(mContext, LoadService.class));
        } else {
            getMvpView().showLoading(false);
            getMvpView().showInternetUnavailableError();
        }
    }

    public void getFilms(boolean pullToRefresh) {
        checkViewAttached();
        mFilmsSyncAdapter.getSyncAccount(mContext);
        if (mDataManager.getPreferencesHelper().checkIfFirstLaunched()) {
            loadFilms(pullToRefresh);
            mDataManager.getPreferencesHelper().markFirstLaunched();
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.getFilms()
                .subscribeOn(Schedulers.newThread())
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FilmModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showUnknownError();
                        Timber.e("Error occurred!", e);
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(List<FilmModel> films) {
                        if (films.isEmpty()) {
                            getMvpView().showUnknownError();
                        } else {
                            getMvpView().showFilmsList(films);
                        }
                    }
                });
    }
}
