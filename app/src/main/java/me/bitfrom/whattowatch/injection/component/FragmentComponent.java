package me.bitfrom.whattowatch.injection.component;

import android.app.Fragment;

import dagger.Component;
import dagger.Subcomponent;
import me.bitfrom.whattowatch.injection.PerFragment;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.injection.module.FragmentModule;
import me.bitfrom.whattowatch.ui.fragments.BottomFilmsFragment;
import me.bitfrom.whattowatch.ui.fragments.ComingSoonFragment;
import me.bitfrom.whattowatch.ui.fragments.FavoritesFragment;
import me.bitfrom.whattowatch.ui.fragments.InCinemasFragment;
import me.bitfrom.whattowatch.ui.fragments.TopFilmsFragment;

@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(Fragment fragment);

    void inject(TopFilmsFragment topFilmsFragment);

    void inject(FavoritesFragment favoritesFragment);

    void inject(BottomFilmsFragment bottomFilmsFragment);

    void inject(InCinemasFragment inCinemasFragment);

    void inject(ComingSoonFragment comingSoonFragment);

}