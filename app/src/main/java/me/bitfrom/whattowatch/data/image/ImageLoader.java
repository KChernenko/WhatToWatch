package me.bitfrom.whattowatch.data.image;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import javax.inject.Inject;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.injection.ActivityContext;

public class ImageLoader implements ImageLoaderInteractor {

    private Context mContext;

    @Inject
    public ImageLoader(@ActivityContext Context context) {
        mContext = context;
    }

    @Override
    public void loadImage(@Flag int flag, String imageUrl, ImageView target) {
        switch (flag) {
            case Flag.LOAD_ONLY:
                downloadOnly(imageUrl, target);
                break;
            case Flag.RECYCLER_ITEM:
                loadPosterItem(imageUrl, target);
                break;
            case Flag.FULL_SIZE:
                downloadFullSize(imageUrl, target);
                break;
            default:
                break;
        }
    }

    private void downloadOnly(String imageUrl, ImageView target) {
        Glide.with(mContext).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(target);
    }

    private void loadPosterItem(String imageUrl, ImageView target) {
        Glide.with(mContext).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.progress_animation)
                .into(target);
    }

    private void downloadFullSize(String imageUrl, ImageView target) {
        Glide.with(mContext).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.progress_animation)
                .into(target);
    }
}
