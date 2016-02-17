package me.bitfrom.whattowatch.injection.component;

import android.app.Fragment;

import dagger.Component;
import me.bitfrom.whattowatch.injection.PerFragment;
import me.bitfrom.whattowatch.injection.module.FragmentModule;

@PerFragment
@Component(dependencies = ActivityComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(Fragment fragment);

}
