package me.bitfrom.whattowatch.domain.weapons.network;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.domain.contracts.ImageDownloadInteractor;

/**
 * Created by Constantine with love.
 */
public class ImageDownloadWeapon implements ImageDownloadInteractor{

    public void loadPoster(Context context, String posterUrl, ImageView target, FLAG flag) {
        if (flag == FLAG.LOAD) {
            downloadPoster(context, posterUrl);
        } else if (flag == FLAG.INSERT) {
            insertPoster(context, posterUrl, target);
        }
    }

    private void downloadPoster(Context context, String posterUrl) {
        Picasso.with(context).load(posterUrl);
    }

    private void insertPoster(Context context, String posterUrl, ImageView target) {
        Picasso.with(context).load(posterUrl).placeholder(R.drawable.progress_animation).into(target);
    }
}
