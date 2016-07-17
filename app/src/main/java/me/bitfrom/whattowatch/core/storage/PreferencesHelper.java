package me.bitfrom.whattowatch.core.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.utils.ConstantsManager;

@Singleton
public class PreferencesHelper {

    private final SharedPreferences privatePref;
    private static SharedPreferences pref;
    private Context context;

    @Inject
    public PreferencesHelper(@NonNull @ApplicationContext Context context) {
        privatePref = context.getSharedPreferences(ConstantsManager.PREF_FILE_NAME, Context.MODE_PRIVATE);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return pref;
    }

    public boolean checkIfFirstLaunched() {
        return privatePref.getBoolean(ConstantsManager.APPS_FIRST_LAUNCH, true);
    }

    public void markFirstLaunched() {
        privatePref.edit().putBoolean(ConstantsManager.APPS_FIRST_LAUNCH, false).apply();
    }

    /**
     * Returns number of films, that user selected in the application's settings;
     * 7 - by default.
     **/
    public int getProfferedNuderOfFilms() {
        return Integer.parseInt(pref.getString(context.getString(R.string.pref_number_of_films_key),
                context.getString(R.string.pref_number_of_films_seven)));
    }

    /**
     * Returns number of days for syncing, that user selected in the app's settings;
     * 5 - by default.
     **/
    public static int getUpdateInterval(@NonNull Context context) {
        int updateInterval;
        int updatePeriod = Integer.parseInt(pref
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
        privatePref.edit().clear().apply();
    }
}