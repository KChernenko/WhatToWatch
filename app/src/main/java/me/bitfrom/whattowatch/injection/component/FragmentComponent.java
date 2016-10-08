package me.bitfrom.whattowatch.injection.component;

import android.app.Fragment;

import dagger.Subcomponent;
import me.bitfrom.whattowatch.injection.PerFragment;
import me.bitfrom.whattowatch.injection.module.FragmentModule;
import me.bitfrom.whattowatch.ui.bottomfilms.BottomFilmsFragment;
import me.bitfrom.whattowatch.ui.comingsoon.ComingSoonFragment;
import me.bitfrom.whattowatch.ui.favorites.FavoritesFragment;
import me.bitfrom.whattowatch.ui.incinemas.InCinemasFragment;
import me.bitfrom.whattowatch.ui.topfilms.TopFilmsFragment;

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