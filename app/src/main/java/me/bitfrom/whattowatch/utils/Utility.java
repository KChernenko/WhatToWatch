package me.bitfrom.whattowatch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.bitfrom.whattowatch.R;

/**
 * Created by Constantine with love.
 */
public class Utility {

    public static final String ID_KEY = "position_id";

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
                updateInterval = 60 * 4320;
                break;
            case 5:
                updateInterval = 60 * 7200;
                break;
            case 7:
                updateInterval = 60 * 10080;
                break;
            default:
                updateInterval = 60 *  7200;
        }

        return updateInterval;
    }

}
