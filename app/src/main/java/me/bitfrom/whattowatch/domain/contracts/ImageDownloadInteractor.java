package me.bitfrom.whattowatch.domain.contracts;

import android.widget.ImageView;


/**
 * Created by Constantine with love.
 */
public interface ImageDownloadInteractor {

    enum FLAG {LOAD, FULL_SIZE, CROPPED_SIZE}

    void loadPoster(FLAG flag, String posterUrl, ImageView target);

}
