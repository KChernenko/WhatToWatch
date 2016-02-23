package me.bitfrom.whattowatch.data;

import android.app.IntentService;
import android.content.Intent;

import com.bumptech.glide.Glide;

import me.bitfrom.whattowatch.utils.ConstantsManager;

public class CacheCleanerService extends IntentService{

    public CacheCleanerService() {
        super(ConstantsManager.CACHE_CLEANER_SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Glide.get(this).clearDiskCache();
    }
}
