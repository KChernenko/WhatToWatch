package me.bitfrom.whattowatch.injection.component;

import dagger.Subcomponent;
import me.bitfrom.whattowatch.injection.PerActivity;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.injection.module.FragmentModule;
import me.bitfrom.whattowatch.ui.details.DetailActivity;
import me.bitfrom.whattowatch.ui.main.MainActivity;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    FragmentComponent addFragmentComponent(FragmentModule fragmentModule);

    void inject(MainActivity mainActivity);

    void inject(DetailActivity detailActivity);
    
}