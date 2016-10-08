package me.bitfrom.whattowatch.ui.comingsoon;

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

public class ComingSoonPresenter extends BasePresenter<ComingSoonMvpView> {

    private final DataManager dataManager;
    private EventBus eventBus;

    private CompositeSubscription subscription;

    @Inject
    public ComingSoonPresenter(@NonNull DataManager dataManager,
                               @NonNull EventBus eventBus) {
        this.dataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(@NonNull ComingSoonMvpView mvpView) {
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
        Timber.d("Start loading coming soon films...");
        subscription.add(dataManager.loadComingSoonFilms()
                .subscribeOn(Schedulers.io())
                .replay().autoConnect()
                .doAfterTerminate(() -> Timber.d("Loading coming soon films has finished!"))
                .subscribe(movie -> {

                }, throwable -> Timber.e(throwable, "Error occurred while loading coming soon films!")));
    }

    public void getFilms() {
        subscription.add(dataManager.getComingSoonFilms()
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