package me.bitfrom.whattowatch.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;

import javax.inject.Inject;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class NotificationHelper {

    private DataManager mDataManager;
    private Context mContext;

    @Inject
    public NotificationHelper(DataManager dataManager, @ApplicationContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    public void showNotification() {
        //checking the last update and notify if it' the first of the day
        String displayNotificationsKey = mContext.getString(R.string.pref_enable_notifications_key);
        String vibNotificationsKey = mContext.getString(R.string.pref_enable_vibration_key);
        String ledNotificationsKey = mContext.getString(R.string.pref_enable_led_key);
        String soundNotificationsKey = mContext.getString(R.string.pref_enable_sound_key);

        boolean displayNotifications = mDataManager.getPreferencesHelper().getSharedPreferences()
                .getBoolean(displayNotificationsKey, Boolean.parseBoolean(mContext.getString(R.string.pref_enable_notifications_default)));
        boolean ledNotifications = mDataManager.getPreferencesHelper().getSharedPreferences()
                .getBoolean(ledNotificationsKey, Boolean.parseBoolean(mContext.getString(R.string.pref_enable_notifications_default)));
        boolean vibNotifications = mDataManager.getPreferencesHelper().getSharedPreferences()
                .getBoolean(vibNotificationsKey, Boolean.parseBoolean(mContext.getString(R.string.pref_enable_notifications_default)));
        boolean soundNotifications = mDataManager.getPreferencesHelper().getSharedPreferences()
                .getBoolean(soundNotificationsKey, Boolean.parseBoolean(mContext.getString(R.string.pref_enable_notifications_default)));

        if (displayNotifications) {
            android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(mContext);
            //Create Intent to launch this Activity again if the notification is clicked.
            Intent i = new Intent(mContext, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(intent);

            builder.setSmallIcon(R.mipmap.ic_launcher);

            if (ledNotifications) {
                builder.setLights(Color.CYAN, 300, 1500);
            }
            if (vibNotifications) {
                builder.setVibrate(new long[]{500, 500});
            }
            if (soundNotifications) {
                builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            }

            // Cancel the notification when clicked
            builder.setAutoCancel(true);

            String title = mContext.getString(R.string.app_name);
            String contentText = mContext.getResources().getString(R.string.notification_message);
            Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
            builder.setLargeIcon(largeIcon);
            builder.setContentTitle(title);
            builder.setContentText(contentText);

            Notification notification = builder.build();
            NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(ConstantsManager.NOTIFICATION_ID, notification);
        }
    }
}
