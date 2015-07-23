package me.bitfrom.whattowatch.utils;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by Constantine with love.
 */
public class DownloadImageWeapon {

    public static void downloadPoster(Context context, String posterUrl) {
        Picasso.with(context).load(posterUrl);
    }
}
