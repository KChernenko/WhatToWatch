package me.bitfrom.whattowatch.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import me.bitfrom.whattowatch.MainActivity;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.rest.RestService;
import me.bitfrom.whattowatch.rest.model.Movie;
import me.bitfrom.whattowatch.utils.Utility;

import static me.bitfrom.whattowatch.data.MoviesContract.*;

/**
 * Created by Constantine with love.
 */
public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = MoviesSyncAdapter.class.getSimpleName();


    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    private static final int MOVIE_NOTIFICATION_ID = 3002;

    private List<Movie> moviesList = new ArrayList<>();
    private Movie movieData;


    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

        getData();
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name),
                context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {
            /*
            * Add the account and account type, no password or user data
            * If successful, return the Account object, otherwise report an error.
            */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
             /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);
        }

        return newAccount;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    private static void onAccountCreated(Account newAccount, Context context) {

        final int SYNC_INTERVAL = Utility.getUpdateInterval(context);
        final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
        /*
         * Since we've created an account
         */
        MoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    private void getData() {
        int numberOfMovies = Utility.getPreferredNumbersOfMovies(getContext());

        if (moviesList.isEmpty()) {
            RestService restService = new RestService();
            Log.d(LOG_TAG, "First: " + moviesList.size());
            while (moviesList.size() < numberOfMovies) {
                movieData = restService.request();
                Log.d(LOG_TAG, movieData.getTitle());
                moviesList.add(movieData);
            }
            Log.d(LOG_TAG, "Second: " + moviesList.size());

            if (moviesList.size() == numberOfMovies) {
                Vector<ContentValues> cVVector = new Vector<>(numberOfMovies);

                Time dayTime = new Time();
                dayTime.setToNow();

                // we start at the day returned by local time. Otherwise this is a mess.
                int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                // now we work exclusively in UTC
                dayTime = new Time();

                int timeCounter = 0;

                for (Movie movie: moviesList) {
                    ContentValues movieValues = new ContentValues();

                    timeCounter++;

                    long dateTime = dayTime.setJulianDay(julianStartDay + timeCounter);

                    movieValues.put(MoviesEntry.COLUMN_TITLE, movie.getTitle());
                    movieValues.put(MoviesEntry.COLUMN_DIRECTORS, movie.getDirectors().get(0).getName());
                    movieValues.put(MoviesEntry.COLUMN_GENRES, movie.getGenres().toString());
                    movieValues.put(MoviesEntry.COLUMN_WRITERS, movie.getWriters().get(0).getName());
                    movieValues.put(MoviesEntry.COLUMN_COUNTRIES, movie.getCountries().get(0));
                    movieValues.put(MoviesEntry.COLUMN_YEAR, movie.getYear());
                    movieValues.put(MoviesEntry.COLUMN_RUNTIME, movie.getRuntime().get(0));
                    movieValues.put(MoviesEntry.COLUMN_URL_POSTER, movie.getUrlPoster());
                    movieValues.put(MoviesEntry.COLUMN_RATING, movie.getRating());
                    movieValues.put(MoviesEntry.COLUMN_PLOT, movie.getPlot());
                    movieValues.put(MoviesEntry.COLUMN_URL_IMDB, movie.getUrlIMDB());
                    movieValues.put(MoviesEntry.COLUMN_DATE, dateTime);

                    downloadPosters(movie.getUrlPoster());

                    cVVector.add(movieValues);
                }

                //add to database
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    int inserted = getContext().getContentResolver()
                            .bulkInsert(MoviesEntry.CONTENT_URI, cvArray);
                    Log.d(LOG_TAG, "Inserted: " + inserted);

                    // delete old data so we don't build up an endless history
                    int deleted = getContext().getContentResolver().delete(MoviesEntry.CONTENT_URI,
                            MoviesEntry.COLUMN_DATE + " <= ?",
                            new String[] {Long.toString(dayTime.setJulianDay(julianStartDay-1))});
                    Log.d(LOG_TAG, "Deleted: " + deleted);

                    updateNotifications();
                }
            }
        }

    }

    private void updateNotifications() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if (displayNotifications) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
            //Create Intent to launch this Activity again if the notification is clicked.
            Intent i = new Intent(getContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(getContext(), 0, i,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(intent);
            // Sets the small icon for the ticker
            builder.setSmallIcon(R.drawable.ic_theaters_white_24dp);
            // Sets the indicator
            builder.setLights(Color.MAGENTA, 1, 4);
            // Sets vibration
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            // Sets default sound
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            // Cancel the notification when clicked
            builder.setAutoCancel(true);

            String title = context.getString(R.string.app_name);
            String contentText = getContext().getResources().getString(R.string.notification_message);
            Bitmap largeIcon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.ic_theaters_white_48dp);
            builder.setLargeIcon(largeIcon);
            builder.setContentTitle(title);
            builder.setContentText(contentText);
            // Build the notification
            Notification notification = builder.build();

            // Use the NotificationManager to show the notification
            NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(MOVIE_NOTIFICATION_ID, notification);
        }
    }

    private void downloadPosters(String posterUrl) {
        Picasso.with(getContext()).load(posterUrl);
    }
}
