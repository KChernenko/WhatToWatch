package me.bitfrom.whattowatch.ui.topfilms;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.busevents.ServerErrorEvent;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class TopFilmsPresenter extends BasePresenter<TopFilmsMvpView> {

    private final DataManager dataManager;
    private EventBus eventBus;

    private CompositeSubscription subscription;

    @Inject
    public TopFilmsPresenter(@NonNull DataManager dataManager,
                             @NonNull EventBus eventBus) {
        this.dataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(@NonNull TopFilmsMvpView mvpView) {
        super.attachView(mvpView);
        eventBus.register(this);
        subscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        eventBus.unregister(this);
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
    }

    public void loadFilms() {
        Timber.d("Start loading top films...");
        subscription.add(dataManager.loadTopFilms()
                .subscribeOn(Schedulers.io())
                .replay().autoConnect()
                .doAfterTerminate(() -> Timber.d("Loading top films has finished!"))
                .subscribe(movie -> {

                }, throwable -> Timber.e(throwable, "Error occurred while loading top films!")));
    }

    public void getFilms() {
        subscription.add(dataManager.getTopFilms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(films -> {
                    if (films.isEmpty()) {
                        if (isViewAttached()) getMvpView().showListIsEmpty();
                        loadFilms();
                    } else {
                        if (isViewAttached()) {
                            getMvpView().showLoading(false);
                            getMvpView().showFilmsList(films);
                        }
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getMvpView().showLoading(false);
                        getMvpView().showServerError();
                    }
                    Timber.e(throwable, "Error occurred!");
                }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serverErrorEventHandler(ServerErrorEvent event) {
        checkViewAttached();
        getMvpView().showLoading(false);
        getMvpView().showServerError();
    }
}