package me.bitfrom.whattowatch.ui.fragments.presenters;

import android.content.Context;
import android.content.Intent;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.LoadService;
import me.bitfrom.whattowatch.data.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.ui.fragments.views.RandomFilmsMvpView;
import me.bitfrom.whattowatch.utils.NetworkStateChecker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class RandomFilmsPresenter extends BasePresenter<RandomFilmsMvpView> {

    private final DataManager mDataManager;
    private Context mContext;
    private Subscription mSubscription;

    @Inject
    public RandomFilmsPresenter(DataManager dataManager, @ApplicationContext Context context) {
        mDataManager = dataManager;
        mContext = context;
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

    public void getFilms() {
        checkViewAttached();
        if (mDataManager.getPreferencesHelper().checkIfFirstLaunched()) {
            FilmsSyncAdapter.initSyncAdapter(mContext);
            mDataManager.getPreferencesHelper().markFirstLaunched();
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.getFilms()
                .subscribeOn(Schedulers.io())
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(films -> {
                    if (films.isEmpty()) {
                        getMvpView().showUnknownError();
                        loadFilms(true);
                    } else {
                        getMvpView().showFilmsList(films);
                    }
                }, throwable -> {
                    getMvpView().showUnknownError();
                    Timber.e("Error occurred!", throwable);
                    if (BuildConfig.DEBUG) {
                        throwable.printStackTrace();
                    }
                });
    }
}
