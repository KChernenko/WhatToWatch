package me.bitfrom.whattowatch.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.provider.Settings;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.utils.ConstantsManager;

/**
 * Created by Constantine with love.
 */
public class NotificationWeapon {

    public static void updateNotifications(Context context) {
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        String vibNotificationsKey = context.getString(R.string.pref_enable_vibration_key);
        String ledNotificationsKey = context.getString(R.string.pref_enable_led_key);
        String soundNotificationsKey = context.getString(R.string.pref_enable_sound_key);

        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey, Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));
        boolean ledNotifications = prefs.getBoolean(ledNotificationsKey, Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));
        boolean vibNotifications = prefs.getBoolean(vibNotificationsKey, Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));
        boolean soundNotifications = prefs.getBoolean(soundNotificationsKey, Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if (displayNotifications) {
            android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
            //Create Intent to launch this Activity again if the notification is clicked.
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
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

            String title = context.getString(R.string.app_name);
            String contentText = context.getResources().getString(R.string.notification_message);
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            builder.setLargeIcon(largeIcon);
            builder.setContentTitle(title);
            builder.setContentText(contentText);

            Notification notification = builder.build();
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(ConstantsManager.NOTIFICATION_ID, notification);
        }
    }
}
