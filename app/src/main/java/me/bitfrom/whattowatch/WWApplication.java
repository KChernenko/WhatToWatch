package me.bitfrom.whattowatch;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;
import me.bitfrom.whattowatch.injection.component.ApplicationComponent;
import me.bitfrom.whattowatch.injection.component.DaggerApplicationComponent;
import me.bitfrom.whattowatch.injection.module.ApplicationModule;
import timber.log.Timber;


public class WWApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        //Because of memory leak
        Stetho.initializeWithDefaults(this);

        mRefWatcher = LeakCanary.install(this);
        initImageLibrary();
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

    public static RefWatcher getRefWatcher(Context context) {
        WWApplication application = (WWApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    /**
     * Init all needed settings for an image library
     **/
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
