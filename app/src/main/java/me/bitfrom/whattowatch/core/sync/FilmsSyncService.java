package me.bitfrom.whattowatch.core.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import timber.log.Timber;

public class FilmsSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static FilmsSyncAdapter sMoviesSyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate - WhatToWatchSyncService");
        synchronized (sSyncAdapterLock) {
            if (sMoviesSyncAdapter == null) {
                sMoviesSyncAdapter = new FilmsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sMoviesSyncAdapter.getSyncAdapterBinder();
    }
}