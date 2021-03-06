package me.bitfrom.whattowatch;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.JobManager;
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
import me.bitfrom.whattowatch.core.sync.SyncFilmsJobCreator;
import me.bitfrom.whattowatch.injection.component.ApplicationComponent;
import me.bitfrom.whattowatch.injection.component.DaggerApplicationComponent;
import me.bitfrom.whattowatch.injection.module.ApplicationModule;
import timber.log.Timber;

public class WWApplication extends Application {

    private ApplicationComponent applicationComponent;
    private RefWatcher refWatcher;
    private static WWApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        initTimber();
        initFabric();
        initLeakCanary();
        initImageLibrary();
        initStetho();
        initAndroidJob();
    }

    public static WWApplication getApplication() {
        return application;
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }

        return applicationComponent;
    }

    public static RefWatcher getRefWatcher(Context context) {
        WWApplication application = (WWApplication) context.getApplicationContext();
        return application.refWatcher;
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

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initFabric() {
        Fabric.with(this, new Crashlytics());
    }

    private void initLeakCanary() {
        refWatcher = LeakCanary.install(this);
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void initAndroidJob() {
        JobManager.create(this).addJobCreator(new SyncFilmsJobCreator());
    }
}