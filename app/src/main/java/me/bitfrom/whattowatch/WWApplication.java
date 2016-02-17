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

import org.greenrobot.eventbus.EventBus;

import io.fabric.sdk.android.Fabric;
import me.bitfrom.whattowatch.injection.component.ApplicationComponent;
import me.bitfrom.whattowatch.injection.component.DaggerApplicationComponent;
import me.bitfrom.whattowatch.injection.module.ApplicationModule;
import me.bitfrom.whattowatch.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.utils.NetworkStateChecker;
import me.bitfrom.whattowatch.utils.bus.ConnectionUnsuccessfulEvent;


public class WWApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    public static Context context;

    private SharedPreferences pref = null;
    private static final String FIRST_LAUNCH = "app_first_launch";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        WWApplication.context = getApplicationContext();

        initImageLibrary();

        loadDataIfFirstLaunch();
    }

    public static Context getAppContext() {
        return context;
    }

    public static WWApplication get(Context context) {
        return (WWApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }

        return mApplicationComponent;
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
                pref.edit().putBoolean(FIRST_LAUNCH, false).apply();
            } else {
                EventBus.getDefault()
                        .post(new ConnectionUnsuccessfulEvent(getString(R.string.error_connection_unavailable)));
            }
        }
    }

    /***
     * Inits all needed settings for an image library
     ***/
    private void initImageLibrary() {
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
    }
}
