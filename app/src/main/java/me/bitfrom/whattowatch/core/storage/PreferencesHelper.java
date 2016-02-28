package me.bitfrom.whattowatch.core.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.utils.ConstantsManager;

@Singleton
public class PreferencesHelper {

    private final SharedPreferences mPrivatePref;
    private static SharedPreferences mPref;
    private Context mContext;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPrivatePref = context.getSharedPreferences(ConstantsManager.PREF_FILE_NAME, Context.MODE_PRIVATE);
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mContext = context;
    }

    public SharedPreferences getSharedPreferences() {
        return mPref;
    }

    public boolean checkIfFirstLaunched() {
        return mPrivatePref.getBoolean(ConstantsManager.APPS_FIRST_LAUNCH, true);
    }

    public void markFirstLaunched() {
        mPrivatePref.edit().putBoolean(ConstantsManager.APPS_FIRST_LAUNCH, false).apply();
    }

    /**
     * Returns number of films, that user selected in the application's settings;
     * 7 - by default.
     **/
    public int getPrefferedNuberOfFilms() {
        return Integer.parseInt(mPref.getString(mContext.getString(R.string.pref_number_of_films_key),
                mContext.getString(R.string.pref_number_of_films_seven)));
    }

    /**
     * Returns number of days for syncing, that user selected in the app's settings;
     * 5 - by default.
     **/
    public static int getUpdateInterval(Context context) {
        int updateInterval;
        int updatePeriod = Integer.parseInt(mPref
                .getString(context.getString(R.string.pref_frequency_of_updates_key),
                        context.getString(R.string.pref_frequency_of_updates_five)));

        switch (updatePeriod) {
            case ConstantsManager.THREE_DAYS:
                updateInterval = ConstantsManager.THREE_DAYS;
                break;
            case ConstantsManager.FIVE_DAYS:
                updateInterval = ConstantsManager.FIVE_DAYS;
                break;
            case ConstantsManager.SEVEN_DAYS:
                updateInterval = ConstantsManager.SEVEN_DAYS;
                break;
            default:
                updateInterval = ConstantsManager.FIVE_DAYS;
        }

        return updateInterval;
    }

    public void clear() {
        mPrivatePref.edit().clear().apply();
    }
}
