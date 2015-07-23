package me.bitfrom.whattowatch.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.rest.RestService;
import me.bitfrom.whattowatch.rest.model.Movie;
import me.bitfrom.whattowatch.utils.DownloadImageWeapon;
import me.bitfrom.whattowatch.utils.MovieGenerator;
import me.bitfrom.whattowatch.utils.NotificationWeapon;
import me.bitfrom.whattowatch.utils.Utility;

import static me.bitfrom.whattowatch.data.MoviesContract.*;

/**
 * Created by Constantine with love.
 */
public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String LOG_TAG = MoviesSyncAdapter.class.getSimpleName();


    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    private List<Movie> requestContainer = new ArrayList<>();

    private List<Movie> moviesList = new ArrayList<>();

    private HashSet<Integer> randomNumbers = new HashSet<>();


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

            makeRequest(numberOfMovies);

            if (moviesList.size() == numberOfMovies) {
                Vector<ContentValues> cVVector = new Vector<>(numberOfMovies);

                GregorianCalendar calendar = new GregorianCalendar();
                long dateTime = calendar.getTimeInMillis();

                for (Movie movie: moviesList) {
                    ContentValues movieValues = new ContentValues();

                    Log.d(LOG_TAG, " " + movie.getTitle());
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

                    DownloadImageWeapon.downloadPoster(getContext(), movie.getUrlPoster());

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
                            MoviesEntry.COLUMN_DATE + " < ?",
                            new String[] {Long.toString(dateTime)});
                    Log.d(LOG_TAG, "Deleted: " + deleted);

                    NotificationWeapon.updateNotifications(getContext());
                }
            }
        }

    }

    private void makeRequest(int numberOfMovies) {
        RestService restService = new RestService();
        requestContainer = restService.request();

        while (randomNumbers.size() < numberOfMovies) {
            randomNumbers.add(MovieGenerator.getGenerator().getRandomMovieID());
        }

        initMoviesContainer(randomNumbers);
    }

    private void initMoviesContainer(HashSet<Integer> randomNumbers) {
        for (Integer randomItem: randomNumbers) {
            moviesList.add(requestContainer.get(randomItem));
        }
    }
}
