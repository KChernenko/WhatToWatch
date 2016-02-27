package me.bitfrom.whattowatch.injection.module;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.whattowatch.data.image.ImageDownloader;
import me.bitfrom.whattowatch.injection.ApplicationContext;

@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context providesContext() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    @Singleton
    ImageDownloader providesImageDownloader() {
        return new ImageDownloader();
    }
}
