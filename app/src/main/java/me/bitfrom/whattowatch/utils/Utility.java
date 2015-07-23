package me.bitfrom.whattowatch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.bitfrom.whattowatch.R;

/**
 * Created by Constantine with love.
 */
public class Utility {

    private static final int THREE_DAYS = 259200;
    private static final int FIVE_DAYS = 432000;
    private static final int SEVEN_DAYS = 604800;

    public static int getPreferredNumbersOfMovies(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int numberOfMovies = Integer.parseInt(preferences
                .getString(context.getString(R.string.pref_number_of_movies_key),
                        context.getString(R.string.pref_number_of_movies_seven)));

        return numberOfMovies;
    }

    public static int getUpdateInterval(Context context) {
        int updateInterval;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int updatePeriod = Integer.parseInt(preferences
                .getString(context.getString(R.string.pref_frequency_of_updates_key),
                        context.getString(R.string.pref_frequency_of_updates_five)));

        switch (updatePeriod) {
            case 3:
                updateInterval = THREE_DAYS;
                break;
            case 5:
                updateInterval = FIVE_DAYS;
                break;
            case 7:
                updateInterval = SEVEN_DAYS;
                break;
            default:
                updateInterval = FIVE_DAYS;
        }

        return updateInterval;
    }
}
