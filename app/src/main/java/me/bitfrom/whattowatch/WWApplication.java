package me.bitfrom.whattowatch;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
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

        mRefWatcher = LeakCanary.install(this);
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
}
