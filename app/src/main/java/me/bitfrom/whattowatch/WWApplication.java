package me.bitfrom.whattowatch;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.L;

import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;
import me.bitfrom.whattowatch.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.utils.NetworkStateChecker;
import me.bitfrom.whattowatch.utils.bus.ConnectionUnsuccessfulEvent;


public class WWApplication extends Application {

    private static Context context;

    private SharedPreferences pref = null;
    private static final String FIRST_LAUNCH = "app_first_launch";

    private static final String TOKEN = "19c94797-333b-45b7-aded-4bdca4e857fa";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        WWApplication.context = getApplicationContext();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        L.writeLogs(false);
        ImageLoader.getInstance().init(config);

        loadDataIfFirstLaunch();
    }

    public static Context getAppContext() {
        return WWApplication.context;
    }


    public static String getToken() {
        return TOKEN;
    }

    /**
     * Checks if app launched first time, if true - init SyncAdapter,
     * that load data immediately. If device haven't connection -
     * send an event to the main fragment.
     * **/
    private void loadDataIfFirstLaunch() {
        pref = getSharedPreferences(getResources().getString(R.string.content_authority),
                MODE_PRIVATE);

        if (pref.getBoolean(FIRST_LAUNCH, true)) {
            FilmsSyncAdapter.initializeSyncAdapter(this);
            if (NetworkStateChecker.isNetworkAvailable(this)) {
                pref.edit().putBoolean(FIRST_LAUNCH, false).commit();
            } else {
                EventBus.getDefault()
                        .post(new ConnectionUnsuccessfulEvent(getString(R.string.error_connection_unavailable)));
            }
        }
    }
}
