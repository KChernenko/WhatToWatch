package me.bitfrom.whattowatch.core.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.ECLAIR)
public class FilmsSyncAdapter extends AbstractThreadedSyncAdapter {

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public FilmsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

    }

//    @Inject
//    protected PreferencesHelper preferencesHelper;
//
//    public FilmsSyncAdapter(@NonNull @ApplicationContext Context context,
//                            boolean autoInitialize) {
//        super(context, autoInitialize);
//        WWApplication.get(context).getComponent().inject(this);
//    }
//
//    @Override
//    public void onPerformSync(@NonNull Account account, @NonNull Bundle extras,
//                              @NonNull String authority,
//                              @NonNull ContentProviderClient provider,
//                              @NonNull SyncResult syncResult) {
//
//        getContext().startService(new Intent(getContext(), SyncTopFilmsService.class));
//        getContext().startService(new Intent(getContext(), SyncBottomFilmsService.class));
//        getContext().startService(new Intent(getContext(), SyncInCinemasFilmsService.class));
//        getContext().startService(new Intent(getContext(), SyncComingSoonService.class));
//    }
//
//    public static void initSyncAdapter(@NonNull Context context) {
//        getSyncAccount(context);
//    }
//
//    /**
//     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
//     * if the fake account doesn't exist yet.  If we make a new account, we call the
//     * onAccountCreated method so we can initialize things.
//     *
//     * @param context The context used to access the account service
//     * @return a fake account.
//     **/
//    public static Account getSyncAccount(@NonNull Context context) {
//        // Get an instance of the Android account manager
//        AccountManager accountManager =
//                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
//
//        // Create the account type and default account
//        Account newAccount = new Account(context.getString(R.string.app_name),
//                context.getString(R.string.sync_account_type));
//
//        // If the password doesn't exist, the account doesn't exist
//        if ( null == accountManager.getPassword(newAccount) ) {
//            /*
//            * Add the account and account type, no password or user data
//            * If successful, return the Account object, otherwise report an error.
//            */
//            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
//                return null;
//            }
//             /*
//             * If you don't set android:syncable="true" in
//             * in your <provider> element in the manifest,
//             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
//             * here.
//             */
//            onAccountCreated(newAccount, context);
//        }
//
//        return newAccount;
//    }
//
//    /**
//     * Helper method to schedule the sync adapter periodic execution
//     **/
//    public static void configurePeriodicSync(@NonNull Context context, int syncInterval, int flexTime) {
//        Account account = getSyncAccount(context);
//        String authority = context.getString(R.string.content_authority);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // we can enable inexact timers in our periodic sync
//            SyncRequest request = new SyncRequest.Builder().
//                    syncPeriodic(syncInterval, flexTime).
//                    setSyncAdapter(account, authority).
//                    setExtras(new Bundle()).build();
//            ContentResolver.requestSync(request);
//        } else {
//            ContentResolver.addPeriodicSync(account,
//                    authority, new Bundle(), syncInterval);
//        }
//    }
//
//    /**
//     * Helper method to have the sync adapter sync immediately
//     * @param context The context used to access the account service
//     **/
//    public void syncImmediately(@NonNull Context context) {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//        ContentResolver.requestSync(getSyncAccount(context),
//                context.getString(R.string.content_authority), bundle);
//    }
//
//    private static void onAccountCreated(@NonNull Account newAccount, @NonNull Context context) {
//
//        final int SYNC_INTERVAL =
//        final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
//        /*
//         * Since we've created an account
//         */
//        configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
//
//        /*
//         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
//         */
//        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
//        ContentResolver.addPeriodicSync(newAccount, context.getString(R.string.content_authority), Bundle.EMPTY,
//                SYNC_INTERVAL);
//
//        /*
//         * Finally, let's do a sync to get things started
//         */
//        //syncImmediately(context);
//    }

}
