package me.bitfrom.whattowatch.ui.topfilms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.busevents.ServerErrorEvent;
import me.bitfrom.whattowatch.core.services.LoadTopFilmsService;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.ui.topfilms.TopFilmsMvpView;
import me.bitfrom.whattowatch.utils.NetworkStateChecker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class TopFilmsPresenter extends BasePresenter<TopFilmsMvpView> {

    private Context context;
    private final DataManager dataManager;
    private EventBus eventBus;
    private Subscription subscription;

    @Inject
    public TopFilmsPresenter(@NonNull @ApplicationContext Context context,
                             @NonNull DataManager dataManager,
                             @NonNull EventBus eventBus) {
        this.context = context;
        this.dataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(@NonNull TopFilmsMvpView mvpView) {
        super.attachView(mvpView);
        eventBus.register(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        eventBus.unregister(this);
    }

    public void loadFilms(boolean pullToRefresh) {
        checkViewAttached();
        getMvpView().showLoading(pullToRefresh);
        if (NetworkStateChecker.isNetworkAvailable(context)) {
            context.startService(new Intent(context, LoadTopFilmsService.class));
        } else {
            getMvpView().showLoading(false);
            getMvpView().showInternetUnavailableError();
        }
    }

    public void getFilms() {
        checkViewAttached();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = dataManager.getTopFilms()
                .subscribeOn(Schedulers.io())
                .replay().autoConnect()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(films -> {
                    if (films.isEmpty()) {
                        getMvpView().showListIsEmpty();
                        loadFilms(true);
                    } else {
                        getMvpView().showFilmsList(films);
                    }
                }, throwable -> {
                    getMvpView().showLoading(false);
                    getMvpView().showServerError();
                    Timber.e(throwable, "Error occurred!");
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serverErrorEventHandler(ServerErrorEvent event) {
        checkViewAttached();
        getMvpView().showLoading(false);
        getMvpView().showServerError();
    }
}