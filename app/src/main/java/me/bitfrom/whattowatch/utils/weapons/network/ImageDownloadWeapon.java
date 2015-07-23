package me.bitfrom.whattowatch.utils.weapons.network;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by Constantine with love.
 */
public class ImageDownloadWeapon {

    public static void downloadPoster(Context context, String posterUrl) {
        Picasso.with(context).load(posterUrl);
    }
}
