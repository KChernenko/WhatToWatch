package me.bitfrom.whattowatch.injection.component;

import android.app.Fragment;

import dagger.Component;
import me.bitfrom.whattowatch.injection.PerFragment;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.injection.module.FragmentModule;
import me.bitfrom.whattowatch.ui.fragments.DetailFragment;
import me.bitfrom.whattowatch.ui.fragments.FavoritesFragment;
import me.bitfrom.whattowatch.ui.fragments.TopFilmsFragment;

@PerFragment
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, FragmentModule.class})
public interface FragmentComponent {

    void inject(Fragment fragment);

    void inject(TopFilmsFragment topFilmsFragment);

    void inject(DetailFragment detailFragment);

    void inject(FavoritesFragment favoritesFragment);

}
