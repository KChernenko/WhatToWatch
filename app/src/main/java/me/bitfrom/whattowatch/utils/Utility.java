package me.bitfrom.whattowatch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import me.bitfrom.whattowatch.R;

/**
 * Created by Constantine with love.
 */
public class Utility {

    //Three days in seconds
    private static final int THREE_DAYS = 259200;
    //Five days in seconds
    private static final int FIVE_DAYS = 432000;
    //Seven days in seconds
    private static final int SEVEN_DAYS = 604800;

    /**
     * Returns number of films, that user selected in the application's settings;
     * 7 - by default.
     * @param context - used to get the preference from the SharedPreferences
     * @return int
     * **/
    public static int getPreferredNumbersOfMovies(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return Integer.parseInt(preferences
                .getString(context.getString(R.string.pref_number_of_movies_key),
                        context.getString(R.string.pref_number_of_movies_seven)));
    }

    /**
     * Returns number of days for syncing, that user selected in the app's settings;
     * 5 - by default.
     * @param context - used to get the preference from the SharedPreferences
     * @return int
     * **/
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

    /**
     * Returns true if the network is available or about become available
     *
     * @param context used to get the ConnectivityManager
     * @return
     * **/
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}