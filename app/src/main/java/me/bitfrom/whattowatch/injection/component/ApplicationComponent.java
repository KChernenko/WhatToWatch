package me.bitfrom.whattowatch.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.LoadBottomFilmsService;
import me.bitfrom.whattowatch.data.LoadTopFilmsService;
import me.bitfrom.whattowatch.data.storage.DBHelper;
import me.bitfrom.whattowatch.data.storage.PreferencesHelper;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.injection.module.ApplicationModule;
import me.bitfrom.whattowatch.injection.module.RestModule;

@Singleton
@Component(modules = {ApplicationModule.class, RestModule.class})
public interface ApplicationComponent {

    void inject(LoadTopFilmsService loadTopFilmsService);

    void inject(LoadBottomFilmsService loadBottomFilmsService);

    @ApplicationContext
    Context contex();

    Application application();

    PreferencesHelper providesPreferencesHelper();

    DBHelper providesDBHelper();

    DataManager providesDataManager();
}
