package me.bitfrom.whattowatch.data.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.data.DataManager;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoadTopFilmsService extends Service {
    @Inject
    protected DataManager mDataManager;

    private Subscription mSubscription;

    @Override
    public void onCreate() {
        super.onCreate();
        WWApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        Timber.d("Start loading top films...");
        mSubscription = mDataManager.loadTopFilms()
                .subscribeOn(Schedulers.io())
                .doAfterTerminate(() -> {
                    Timber.d("Loading top films has finished!");
                    stopSelf(startId);
                })
                .subscribe(movie -> {

                }, throwable -> {
                    stopSelf(startId);
                    Timber.e(throwable, "Error occurred while loading top films!");
                    if (BuildConfig.DEBUG) {
                        throwable.printStackTrace();
                    }
                });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
