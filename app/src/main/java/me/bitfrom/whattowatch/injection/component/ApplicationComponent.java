package me.bitfrom.whattowatch.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.LoadService;
import me.bitfrom.whattowatch.data.rest.FilmsAPI;
import me.bitfrom.whattowatch.data.storage.DBHelper;
import me.bitfrom.whattowatch.data.storage.PreferencesHelper;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.injection.module.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(LoadService loadService);

    @ApplicationContext
    Context contex();

    Application application();

    FilmsAPI providesFilmsAPI();

    PreferencesHelper providesPreferencesHelper();

    DBHelper providesDBHelper();

    DataManager providesDataManager();
}
