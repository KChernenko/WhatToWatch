package me.bitfrom.whattowatch.injection.component;

import dagger.Component;
import me.bitfrom.whattowatch.injection.PerActivity;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.ui.activity.SplashScreen;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SplashScreen splashScreen);
    
}
