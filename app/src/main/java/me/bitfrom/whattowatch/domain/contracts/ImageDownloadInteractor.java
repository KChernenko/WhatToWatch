package me.bitfrom.whattowatch.domain.contracts;

import android.content.Context;
import android.widget.ImageView;


/**
 * Created by Constantine with love.
 */
public interface ImageDownloadInteractor {

    enum FLAG {LOAD, INSERT}

    void loadPoster(Context context, String posterUrl,
                     ImageView target, FLAG flag);

}
