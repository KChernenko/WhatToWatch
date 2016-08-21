package me.bitfrom.whattowatch.core.image;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import javax.inject.Inject;

import me.bitfrom.whattowatch.R;

public class ImageDownloader implements ImageLoaderInteractor {

    private ImageLoader imageLoader;

    @Inject
    public ImageDownloader() {
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void loadImage(@Flag int flag, @NonNull String imageUrl, ImageView target) {
        switch (flag) {
            case Flag.LOAD_ONLY:
                downloadPoster(imageUrl);
                break;
            case Flag.RECYCLER_ITEM:
                loadCroppedPoster(imageUrl, target);
                break;
            case Flag.FULL_SIZE:
                loadFullSizePoster(imageUrl, target);
                break;
            default:
                break;
        }
    }

    private void downloadPoster(@NonNull String posterUrl) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();


        imageLoader.loadImage(posterUrl, options, null);
    }


    private void loadCroppedPoster(@NonNull String posterUrl, @NonNull ImageView target) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.drawable.progress_animation)
                .cacheInMemory(true)
                .build();


        imageLoader.displayImage(posterUrl, target, options);
    }


    private void loadFullSizePoster(@NonNull String posterUrl, @NonNull ImageView target) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.drawable.progress_animation)
                .build();


        imageLoader.displayImage(posterUrl, target, options);
    }
}