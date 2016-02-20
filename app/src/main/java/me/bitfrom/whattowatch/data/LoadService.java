package me.bitfrom.whattowatch.data;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.data.model.Movie;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class LoadService extends Service {

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
        mSubscription = mDataManager.loadFilms()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Observable<Movie>>() {
                    @Override
                    public void onCompleted() {
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSubscription.unsubscribe();
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Observable<Movie> movieObservable) {

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
