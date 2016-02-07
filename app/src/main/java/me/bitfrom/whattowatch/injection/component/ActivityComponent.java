package me.bitfrom.whattowatch.injection.component;

import dagger.Component;
import me.bitfrom.whattowatch.injection.PerActivity;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.ui.activity.MainActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    
}
