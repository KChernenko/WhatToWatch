package me.bitfrom.whattowatch.injection.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.whattowatch.core.image.ImageDownloader;
import me.bitfrom.whattowatch.injection.ApplicationContext;

@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(@NonNull Application application) {
        mApplication = application;
    }

    @Provides @NonNull
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext @NonNull
    Context providesContext() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    @Singleton @NonNull
    ImageDownloader providesImageDownloader() {
        return new ImageDownloader();
    }

    @Provides
    EventBus providesEventBus() {return EventBus.getDefault();}
}
