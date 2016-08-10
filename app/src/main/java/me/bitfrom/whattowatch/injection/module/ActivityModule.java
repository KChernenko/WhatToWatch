package me.bitfrom.whattowatch.injection.module;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.whattowatch.injection.ActivityContext;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Provides @NonNull
    Activity providesActivity() {
        return activity;
    }

    @Provides
    @ActivityContext @NonNull
    Context providesContext() {
        return activity;
    }
}