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
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.core.storage.PreferencesHelper;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.ui.main.MainActivity;
import me.bitfrom.whattowatch.utils.ConstantsManager;

@Singleton
public class NotificationHelper {

    private Context context;
    private PreferencesHelper preferencesHelper;

    @Inject
    public NotificationHelper(@NonNull @ApplicationContext Context context,
                              @NonNull PreferencesHelper preferencesHelper) {
        this.context = context;
        this.preferencesHelper = preferencesHelper;
    }

    public void showNotification() {
        //checking the last update and notify if it' the first of the day
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        String vibNotificationsKey = context.getString(R.string.pref_enable_vibration_key);
        String ledNotificationsKey = context.getString(R.string.pref_enable_led_key);
        String soundNotificationsKey = context.getString(R.string.pref_enable_sound_key);

        boolean displayNotifications = preferencesHelper.getSharedPreferences()
                .getBoolean(displayNotificationsKey, Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));
        boolean ledNotifications = preferencesHelper.getSharedPreferences()
                .getBoolean(ledNotificationsKey, Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));
        boolean vibNotifications = preferencesHelper.getSharedPreferences()
                .getBoolean(vibNotificationsKey, Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));
        boolean soundNotifications = preferencesHelper.getSharedPreferences()
                .getBoolean(soundNotificationsKey, Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if (displayNotifications) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            //Create Intent to launch this Activity again if the notification is clicked.
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

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