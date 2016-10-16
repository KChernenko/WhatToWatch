package me.bitfrom.whattowatch.injection.component;

import android.app.Application;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;
import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.rest.RestModule;
import me.bitfrom.whattowatch.core.storage.DbHelper;
import me.bitfrom.whattowatch.core.storage.PreferencesHelper;
import me.bitfrom.whattowatch.core.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.core.sync.SyncFilmsJob;
import me.bitfrom.whattowatch.core.sync.services.SyncBottomFilmsService;
import me.bitfrom.whattowatch.core.sync.services.SyncComingSoonService;
import me.bitfrom.whattowatch.core.sync.services.SyncInCinemasFilmsService;
import me.bitfrom.whattowatch.core.sync.services.SyncTopFilmsService;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.injection.module.ApplicationModule;

@Singleton
@Component(modules = {ApplicationModule.class, RestModule.class})
public interface ApplicationComponent {

    ActivityComponent addActivityComponent(ActivityModule activityModule);

    void inject(SyncTopFilmsService syncTopFilmsService);

    void inject(SyncBottomFilmsService syncBottomFilmsService);

    void inject(SyncInCinemasFilmsService syncInCinemasFilmsService);

    void inject(SyncComingSoonService syncComingSoonService);

    void inject(FilmsSyncAdapter filmsSyncAdapter);

    void inject(SyncFilmsJob syncFilmsJob);

    @ApplicationContext
    Context context();

    Application application();

    PreferencesHelper providesPreferencesHelper();

    DbHelper providesDBHelper();

    DataManager providesDataManager();

    EventBus providesEventBus();

    SyncFilmsJob providesSyncFilmsJob();

}