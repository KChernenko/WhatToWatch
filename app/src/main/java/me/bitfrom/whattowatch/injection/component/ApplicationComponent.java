package me.bitfrom.whattowatch.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.injection.module.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context contex();

    Application application();
}
