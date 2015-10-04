package me.bitfrom.whattowatch.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Constantine with love.
 */
public class FilmsSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static FilmsSyncAdapter sMoviesSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("WhatToWatchSyncService", "onCreate - WhatToWatchSyncService");
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

