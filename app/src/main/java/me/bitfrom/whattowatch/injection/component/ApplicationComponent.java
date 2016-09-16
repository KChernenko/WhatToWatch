package me.bitfrom.whattowatch.injection.component;

import android.app.Application;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;
import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.services.LoadBottomFilmsService;
import me.bitfrom.whattowatch.core.services.LoadComingSoonService;
import me.bitfrom.whattowatch.core.services.LoadInCinemasFilmsService;
import me.bitfrom.whattowatch.core.services.LoadTopFilmsService;
import me.bitfrom.whattowatch.core.services.SyncAllService;
import me.bitfrom.whattowatch.core.storage.DHelper;
import me.bitfrom.whattowatch.core.storage.PreferencesHelper;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.injection.module.ApplicationModule;
import me.bitfrom.whattowatch.core.rest.RestModule;

@Singleton
@Component(modules = {ApplicationModule.class, RestModule.class})
public interface ApplicationComponent {

    ActivityComponent addActivityComponent(ActivityModule activityModule);

    void inject(LoadTopFilmsService loadTopFilmsService);

    void inject(LoadBottomFilmsService loadBottomFilmsService);

    void inject(LoadInCinemasFilmsService loadInCinemasFilmsService);

    void inject(LoadComingSoonService loadComingSoonService);

    void inject(SyncAllService syncAllService);

    @ApplicationContext
    Context context();

    Application application();

    PreferencesHelper providesPreferencesHelper();

    DHelper providesDBHelper();

    DataManager providesDataManager();

    EventBus providesEventBus();
}
