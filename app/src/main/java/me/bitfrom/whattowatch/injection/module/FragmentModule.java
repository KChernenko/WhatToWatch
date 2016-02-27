package me.bitfrom.whattowatch.injection.module;

import android.app.Fragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.whattowatch.data.image.ImageDownloader;
import me.bitfrom.whattowatch.injection.ActivityContext;

@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    Fragment providesFragment() {
        return mFragment;
    }

    @Provides
    @ActivityContext
    @Singleton
    ImageDownloader providesImageLoader() {
        return new ImageDownloader();
    }
}
