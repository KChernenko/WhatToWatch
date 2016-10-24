package me.bitfrom.whattowatch.core.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import timber.log.Timber;

@Singleton
public class PreferencesHelper {

    private final SharedPreferences preferences;
    private Context context;

    @Inject
    public PreferencesHelper(@NonNull @ApplicationContext Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(ConstantsManager.PREF_FILE_NAME, Context.MODE_PRIVATE);
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
    public int getPreferredNumberOfFilms() {
        return Integer.parseInt(preferences.getString(context.getString(R.string.pref_number_of_films_key),
                context.getString(R.string.pref_number_of_films_seven)));
    }

    /**
     * Returns number of days for syncing, that user selected in the app's settings;
     * 5 - by default.
     **/
    public long getUpdateInterval() {
        long updateInterval;
        String defaultValue = context.getString(R.string.pref_frequency_of_updates_five);
        int updatePeriod = Integer.parseInt(defaultValue);

        if (! preferences.getString(context.getString(R.string.pref_frequency_of_updates_key), "").isEmpty()) {
            updatePeriod = Integer.parseInt(preferences
                    .getString(context.getString(R.string.pref_frequency_of_updates_key), defaultValue));
        }

        Timber.e("Update period: %s", updatePeriod);

        if (updatePeriod == 3) {
            updateInterval = ConstantsManager.THREE_DAYS;
        } else if (updatePeriod == 5) {
            updateInterval = ConstantsManager.FIVE_DAYS;
        } else if (updatePeriod == 7) {
            updateInterval = ConstantsManager.SEVEN_DAYS;
        } else {
            updateInterval = ConstantsManager.FIVE_DAYS;
        }

        return updateInterval;
    }

    @SuppressLint("CommitPrefEdits")
    public void setUpdateInterval(String interval) {
        preferences.edit().putString(context.getString(R.string.pref_frequency_of_updates_key),
                String.valueOf(interval)).commit();
    }

    @NonNull
    public String getPreferenceByKey(@NonNull String prefKey) {
        return preferences.getString(prefKey, "");
    }
}