package me.bitfrom.whattowatch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

import me.bitfrom.whattowatch.R;

/**
 * Created by Constantine on 05.10.2015.
 */
public class NetworkStateChecker {

    /**
     * Returns true if the network is available or about become available.
     * @param context used to get the ConnectivityManager
     * @return boolean statement
     * **/
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Shows an error if device haven't internet connection.
     * **/
    public static void showErrorMessage(View view) {
        Snackbar.make(view, R.string.error_connection_unavailable,
                Snackbar.LENGTH_LONG).show();
    }
}