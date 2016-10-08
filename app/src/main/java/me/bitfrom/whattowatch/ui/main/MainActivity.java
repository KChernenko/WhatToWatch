package me.bitfrom.whattowatch.ui.main;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.ui.base.BaseActivity;
import me.bitfrom.whattowatch.ui.bottomfilms.BottomFilmsFragment;
import me.bitfrom.whattowatch.ui.comingsoon.ComingSoonFragment;
import me.bitfrom.whattowatch.ui.favorites.FavoritesFragment;
import me.bitfrom.whattowatch.ui.incinemas.InCinemasFragment;
import me.bitfrom.whattowatch.ui.settings.SettingsFragment;
import me.bitfrom.whattowatch.ui.topfilms.TopFilmsFragment;
import me.bitfrom.whattowatch.utils.ConstantsManager;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainMvpView {

    @Inject
    protected MainPresenter mainPresenter;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    protected NavigationView navigationView;

    protected ActionBarDrawerToggle toggle;

    private Bundle bundleArgs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupDrawerLayout();

        mainPresenter.attachView(this);
        if (savedInstanceState == null) {
            bundleArgs = new Bundle();
            replaceFragment(new TopFilmsFragment());
        } else {
            bundleArgs = savedInstanceState;
        }

        mainPresenter.initFirstSync();

        getFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.main_container);
            if (fragment != null) {
                updateToolbarTitle(fragment);
            }
        });
    }

    @Override
    public void onDestroy() {
        drawerLayout.removeDrawerListener(toggle);
        mainPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_favorites:
                FavoritesFragment af = new FavoritesFragment();
                bundleArgs.remove(ConstantsManager.POSITION_ID_KEY);
                replaceFragment(af);
                break;
            case R.id.nav_bottom_films:
                BottomFilmsFragment bff = new BottomFilmsFragment();
                bundleArgs.remove(ConstantsManager.POSITION_ID_KEY);
                replaceFragment(bff);
                break;
            case R.id.nav_in_cinemas:
                InCinemasFragment icf = new InCinemasFragment();
                bundleArgs.remove(ConstantsManager.POSITION_ID_KEY);
                replaceFragment(icf);
                break;
            case R.id.nav_coming_soon:
                ComingSoonFragment csf = new ComingSoonFragment();
                bundleArgs.remove(ConstantsManager.POSITION_ID_KEY);
                replaceFragment(csf);
                break;
            case R.id.nav_settings:
                SettingsFragment sf = new SettingsFragment();
                replaceFragment(sf);
                break;
            default:
                TopFilmsFragment tff = new TopFilmsFragment();
                replaceFragment(tff);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Setup navigation drawer here.
     **/
    private void setupDrawerLayout() {
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * For managing fragments transaction
     **/
    private void replaceFragment(Fragment fragment) {
        String backStateName =  fragment.getClass().getName();

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {

            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    /**
     * Updates toolbar title
     **/
    private void updateToolbarTitle(Fragment fragment) {
        String fragmentClassName = fragment.getClass().getName();

        if (fragmentClassName.equals(TopFilmsFragment.class.getName())) {
            setTitle(getString(R.string.random_films_fragment_title));
            navigationView.setCheckedItem(R.id.nav_top_films);
        } else if (fragmentClassName.equals(FavoritesFragment.class.getName())) {
            setTitle(getString(R.string.drawer_item_favorites));
            navigationView.setCheckedItem(R.id.nav_favorites);
        } else if (fragmentClassName.equals(BottomFilmsFragment.class.getName())) {
            setTitle(getString(R.string.drawer_item_bottom));
            navigationView.setCheckedItem(R.id.nav_bottom_films);
        } else if (fragmentClassName.equals(InCinemasFragment.class.getName())) {
            setTitle(getString(R.string.drawer_item_in_cinemas));
            navigationView.setCheckedItem(R.id.nav_in_cinemas);
        } else if (fragmentClassName.equals(ComingSoonFragment.class.getName())) {
            setTitle(getString(R.string.drawer_item_coming_soon));
            navigationView.setCheckedItem(R.id.nav_coming_soon);
        } else if (fragmentClassName.equals(SettingsFragment.class.getName())) {
            setTitle(getString(R.string.settings_fragment_title));
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }
}