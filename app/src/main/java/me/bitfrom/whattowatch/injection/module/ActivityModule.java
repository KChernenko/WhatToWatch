package me.bitfrom.whattowatch.injection.module;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.whattowatch.injection.ActivityContext;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides @NonNull
    Activity providesActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext @NonNull
    Context providesContext() {
        return mActivity;
    }
}