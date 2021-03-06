package me.bitfrom.whattowatch.ui.bottomfilms;

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

public class BottomFilmsPresenter extends BasePresenter<BottomFilmsMvpView> {

    private final DataManager dataManager;
    private EventBus eventBus;

    private CompositeSubscription subscription;

    @Inject
    public BottomFilmsPresenter(@NonNull DataManager dataManager,
                                @NonNull EventBus eventBus) {
        this.dataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(@NonNull BottomFilmsMvpView mvpView) {
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
        Timber.d("Start loading bottom films ...");
        subscription.add(dataManager.loadBottomFilms()
                .subscribeOn(Schedulers.io())
                .replay().autoConnect()
                .doAfterTerminate(() -> Timber.d("Loading bottom films has finished!"))
                .subscribe(movie -> {

                }, throwable -> Timber.e(throwable, "Error occurred while loading bottom films!")));
    }

    public void getFilms() {
        subscription.add(dataManager.getBottomFilms()
                .replay().autoConnect()
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