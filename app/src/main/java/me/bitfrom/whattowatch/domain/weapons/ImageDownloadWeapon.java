package me.bitfrom.whattowatch.domain.weapons;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.domain.contracts.ImageDownloadInteractor;

/**
 * Created by Constantine with love.
 */
public class ImageDownloadWeapon implements ImageDownloadInteractor{

    private ImageLoader imageLoader;

    public ImageDownloadWeapon() {
        this.imageLoader = ImageLoader.getInstance();
    }

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
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();

        imageLoader.loadImage(posterUrl, options, null);
    }

    private void loadCroppedPoster(String posterUrl, ImageView target) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.drawable.progress_animation)
                .build();

        imageLoader.displayImage(posterUrl, target, options);
    }

    private void loadFullSizePoster(String posterUrl, ImageView target) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.drawable.progress_animation)
                .build();

        imageLoader.displayImage(posterUrl, target, options);
    }
}
