package me.bitfrom.whattowatch.data;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.WWApplication;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoadBottomFilmsService extends Service {
    @Inject
    protected DataManager mDataManager;
    @Inject
    protected NotificationHelper mNotification;

    private Subscription mSubscription;

    @Override
    public void onCreate() {
        super.onCreate();
        WWApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        Timber.d("Start loading bottom films ...");
        getApplication()
                .startService(new Intent(getApplication(), CacheCleanerService.class));
        mSubscription = mDataManager.loadBottomFilms()
                .subscribeOn(Schedulers.io())
                .doAfterTerminate(() -> {
                    Timber.d("Loading bottom films has finished!");
                    stopSelf(startId);
                })
                .subscribe(movie -> {

                }, throwable -> {
                    stopSelf(startId);
                    Timber.e(throwable, "Error occurred while loading bottom films!");
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
