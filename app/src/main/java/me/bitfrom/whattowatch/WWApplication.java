package me.bitfrom.whattowatch;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import de.greenrobot.event.EventBus;
import me.bitfrom.whattowatch.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.utils.NetworkStateChecker;
import me.bitfrom.whattowatch.utils.bus.ConnectionUnsuccessfulEvent;

/**
 * Created by Constantine on 04.10.2015.
 */
public class WWApplication extends Application {

    private static WWApplication application;
    private static Context context;
    private RefWatcher refWatcher;

    private SharedPreferences pref = null;
    private static final String FIRST_LAUNCH = "app_first_launch";

    private static final String TOKEN = "19c94797-333b-45b7-aded-4bdca4e857fa";

    @Override
    public void onCreate() {
        super.onCreate();
        WWApplication.context = getApplicationContext();
        refWatcher = LeakCanary.install(this);

        loadDataIfFirstLaunch();
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
