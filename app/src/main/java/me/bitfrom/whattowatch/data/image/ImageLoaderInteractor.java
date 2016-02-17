package me.bitfrom.whattowatch.data.image;

import android.support.annotation.IntDef;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ImageLoaderInteractor {

    @IntDef({Flag.LOAD_ONLY, Flag.RECYCLER_ITEM, Flag.FULL_SIZE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Flag {
        int LOAD_ONLY = 0;
        int RECYCLER_ITEM = 1;
        int FULL_SIZE = 2;
    }

    void loadImage(@Flag int flag, String imageUrl, ImageView target);
}
