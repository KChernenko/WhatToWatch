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

    protected final Application application;

    public ApplicationModule(@NonNull Application application) {
        this.application = application;
    }

    @Provides @NonNull
    Application providesApplication() {
        return application;
    }

    @Provides
    @ApplicationContext @NonNull
    Context providesContext() {
        return application;
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