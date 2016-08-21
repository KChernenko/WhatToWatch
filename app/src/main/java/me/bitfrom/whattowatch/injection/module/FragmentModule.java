package me.bitfrom.whattowatch.injection.module;

import android.app.Fragment;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.whattowatch.core.image.ImageDownloader;
import me.bitfrom.whattowatch.injection.ActivityContext;

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(@NonNull Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides @NonNull
    Fragment providesFragment() {
        return fragment;
    }

    @Provides
    @ActivityContext
    @Singleton @NonNull
    ImageDownloader providesImageLoader() {
        return new ImageDownloader();
    }
}