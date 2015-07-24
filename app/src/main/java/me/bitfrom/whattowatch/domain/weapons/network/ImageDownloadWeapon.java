package me.bitfrom.whattowatch.domain.weapons.network;

import android.content.Context;

import com.squareup.picasso.Picasso;

import me.bitfrom.whattowatch.domain.contracts.ImageDownloadInteractor;

/**
 * Created by Constantine with love.
 */
public class ImageDownloadWeapon implements ImageDownloadInteractor{

    public void downloadPoster(Context context, String posterUrl) {
        Picasso.with(context).load(posterUrl);
    }
}
