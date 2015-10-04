package me.bitfrom.whattowatch;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Constantine on 04.10.2015.
 */
public class WWApplication extends Application {

    private static WWApplication application;
    private static Context context;
    private RefWatcher refWatcher;

    private static final String TOKEN = "19c94797-333b-45b7-aded-4bdca4e857fa";

    @Override
    public void onCreate() {
        super.onCreate();
        WWApplication.context = getApplicationContext();
        refWatcher = LeakCanary.install(this);
    }

    public static WWApplication get() {
        return application;
    }

    public static Context getAppContext() {
        return WWApplication.context;
    }

    public static RefWatcher getRefWatcher() {
        return WWApplication.get().refWatcher;
    }

    public static String getToken() {
        return TOKEN;
    }
}
