package me.bitfrom.whattowatch.data;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.data.model.Movie;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoadService extends Service {

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
        Timber.d("Start loading...");
        mSubscription = mDataManager.loadFilms()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onCompleted() {
                        getApplication()
                                .startService(new Intent(getApplication(), CacheCleanerService.class));
                        mNotification.showNotification();
                        Timber.d("Loading finished!");
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopSelf(startId);
                        Timber.e(e, "Error occurred!");
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(Movie movie) {

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
