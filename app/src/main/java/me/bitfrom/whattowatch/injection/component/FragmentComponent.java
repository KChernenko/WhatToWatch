package me.bitfrom.whattowatch.injection.component;

import android.app.Fragment;

import dagger.Component;
import me.bitfrom.whattowatch.injection.PerFragment;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.injection.module.FragmentModule;
import me.bitfrom.whattowatch.ui.fragments.RandomFilmsFragment;

@PerFragment
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, FragmentModule.class})
public interface FragmentComponent {

    void inject(Fragment fragment);

    void inject(RandomFilmsFragment fragment);

}
