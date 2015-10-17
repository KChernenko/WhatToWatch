package me.bitfrom.whattowatch.domain.weapons;

import android.widget.ImageView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.domain.contracts.ImageDownloadInteractor;

/**
 * Created by Constantine with love.
 */
public class ImageDownloadWeapon implements ImageDownloadInteractor{

    public void loadPoster(FLAG flag, String posterUrl, ImageView target) {
        switch (flag) {
            case LOAD:
                downloadPoster(posterUrl);
                break;
            case CROPPED_SIZE:
                loadCroppedPoster(posterUrl, target);
                break;
            case FULL_SIZE:
                loadFullSizePoster(posterUrl, target);
                break;
            default:
                break;
        }
    }

    private void downloadPoster(String posterUrl) {
        Picasso.with(WWApplication.getAppContext()).load(posterUrl);
    }

    private void loadCroppedPoster(String posterUrl, ImageView target) {
        Picasso.with(WWApplication.getAppContext())
                .load(posterUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.progress_animation)
                .resize(115, 170)
                .centerCrop()
                .into(target);
    }

    private void loadFullSizePoster(String posterUrl, ImageView target) {
        Picasso.with(WWApplication.getAppContext())
                .load(posterUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.progress_animation)
                .into(target);
    }
}
