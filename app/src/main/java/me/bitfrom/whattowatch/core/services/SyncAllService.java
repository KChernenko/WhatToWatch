package me.bitfrom.whattowatch.core.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.core.NotificationHelper;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class SyncAllService extends IntentService {

    @Inject
    protected NotificationHelper mNotificationHelper;
    @Inject
    @ApplicationContext
    protected Context mContext;

    public SyncAllService() {
        super(ConstantsManager.SYNC_ALL_SERVICE_NAME);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        WWApplication.get(this).getComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext.startService(new Intent(mContext, LoadTopFilmsService.class));
        mContext.startService(new Intent(mContext, LoadBottomFilmsService.class));
        mContext.startService(new Intent(mContext, LoadInCinemasFilmsService.class));
        mContext.startService(new Intent(mContext, LoadComingSoonService.class));

        mNotificationHelper.showNotification();
    }
}