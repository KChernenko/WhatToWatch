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
    protected NotificationHelper notificationHelper;
    
    @Inject
    @ApplicationContext
    protected Context context;

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
        context.startService(new Intent(context, LoadTopFilmsService.class));
        context.startService(new Intent(context, LoadBottomFilmsService.class));
        context.startService(new Intent(context, LoadInCinemasFilmsService.class));
        context.startService(new Intent(context, LoadComingSoonService.class));

        notificationHelper.showNotification();
    }
}