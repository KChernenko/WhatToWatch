package me.bitfrom.whattowatch.domain.contracts;

import android.content.Context;
import android.widget.ImageView;

import me.bitfrom.whattowatch.domain.weapons.network.ImageDownloadWeapon;

/**
 * Created by Constantine with love.
 */
public interface ImageDownloadInteractor {

     void loadPoster(Context context, String posterUrl,
                     ImageView target, ImageDownloadWeapon.FLAG flag);

}
