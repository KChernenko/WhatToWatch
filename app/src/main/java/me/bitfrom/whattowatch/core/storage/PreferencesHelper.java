package me.bitfrom.whattowatch.core.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.utils.ConstantsManager;

@Singleton
public class PreferencesHelper {

    private final SharedPreferences preferences;
    private Context context;

    @Inject
    public PreferencesHelper(@NonNull @ApplicationContext Context context) {
        preferences = context.getSharedPreferences(ConstantsManager.PREF_FILE_NAME, Context.MODE_PRIVATE);
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return preferences;
    }

    public boolean checkIfFirstLaunched() {
        return preferences.getBoolean(ConstantsManager.APPS_FIRST_LAUNCH, true);
    }

    public void markFirstLaunched() {
        preferences.edit().putBoolean(ConstantsManager.APPS_FIRST_LAUNCH, false).apply();
    }

    /**
     * Returns number of films, that user selected in the application's settings;
     * 7 - by default.
     **/
    public int getProfferedNuderOfFilms() {
        return Integer.parseInt(preferences.getString(context.getString(R.string.pref_number_of_films_key),
                context.getString(R.string.pref_number_of_films_seven)));
    }

    /**
     * Returns number of days for syncing, that user selected in the app's settings;
     * 5 - by default.
     **/
    public long getUpdateInterval() {
        long updateInterval;
        long updatePeriod = Long.parseLong(preferences
                .getString(context.getString(R.string.pref_frequency_of_updates_key),
                        context.getString(R.string.pref_frequency_of_updates_five)));

        if (updatePeriod == ConstantsManager.THREE_DAYS) {
            updateInterval = ConstantsManager.THREE_DAYS;
        } else if (updatePeriod == ConstantsManager.FIVE_DAYS) {
            updateInterval = ConstantsManager.FIVE_DAYS;
        } else if (updatePeriod == ConstantsManager.SEVEN_DAYS) {
            updateInterval = ConstantsManager.SEVEN_DAYS;
        } else {
            updateInterval = ConstantsManager.FIVE_DAYS;
        }

        return updateInterval;
    }
}