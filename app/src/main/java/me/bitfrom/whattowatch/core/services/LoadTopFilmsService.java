package me.bitfrom.whattowatch.core.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.core.DataManager;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoadTopFilmsService extends Service {

    @Inject
    protected DataManager dataManager;

    private Subscription subscription;

    @Override
    public void onCreate() {
        super.onCreate();
        WWApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, final int startId) {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        Timber.d("Start loading top films...");
        subscription = dataManager.loadTopFilms()
                .subscribeOn(Schedulers.io())
                .doAfterTerminate(() -> {
                    Timber.d("Loading top films has finished!");
                    stopSelf(startId);
                })
                .subscribe(movie -> {

                }, throwable -> {
                    stopSelf(startId);
                    Timber.e(throwable, "Error occurred while loading top films!");
                });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (subscription != null) subscription.unsubscribe();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}