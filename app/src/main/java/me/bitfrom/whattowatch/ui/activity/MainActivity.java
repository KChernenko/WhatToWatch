package me.bitfrom.whattowatch.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.data.IdTransfer;
import me.bitfrom.whattowatch.ui.base.BaseActivity;
import me.bitfrom.whattowatch.ui.fragments.BottomFilmsFragment;
import me.bitfrom.whattowatch.ui.fragments.DetailFragment;
import me.bitfrom.whattowatch.ui.fragments.FavoritesFragment;
import me.bitfrom.whattowatch.ui.fragments.SettingsFragment;
import me.bitfrom.whattowatch.ui.fragments.TopFilmsFragment;
import me.bitfrom.whattowatch.utils.ConstantsManager;


public class MainActivity extends BaseActivity implements IdTransfer,
        NavigationView.OnNavigationItemSelectedListener, MainMvpView {

    @Inject
    protected MainPresenter mMainPresenter;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    protected NavigationView navigationView;
    @Bind(R.id.coordinator)
    protected CoordinatorLayout coordinatorLayout;

    private Bundle mArgs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mArgs = new Bundle();

        setSupportActionBar(toolbar);
        setupDrawerLayout();

        mMainPresenter.attachView(this);
        if (savedInstanceState == null) {
            replaceFragment(new TopFilmsFragment());
        }

        getFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment f = getFragmentManager().findFragmentById(R.id.main_container);
            if (f != null) {
                updateToolbarTitle(f);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.detachView();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_favorites:
                FavoritesFragment af = new FavoritesFragment();
                mArgs.remove(ConstantsManager.POSITION_ID_KEY);
                replaceFragment(af);
                break;
            case R.id.nav_bottom_films:
                BottomFilmsFragment bff = new BottomFilmsFragment();
                mArgs.remove(ConstantsManager.POSITION_ID_KEY);
                replaceFragment(bff);
                break;
            case R.id.nav_settings:
                SettingsFragment sf = new SettingsFragment();
                replaceFragment(sf);
                break;
            default:
                TopFilmsFragment tff = new TopFilmsFragment();
                replaceFragment(tff);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void sendFilmId(String filmId) {
        DetailFragment df = new DetailFragment();
        mArgs.putString(ConstantsManager.POSITION_ID_KEY, filmId);
        df.setArguments(mArgs);
        replaceFragment(df);
    }

    /**
     * Setup navigation drawer here.
     **/
    private void setupDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
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

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){
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
    private void updateToolbarTitle(Fragment fragment){
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
        } else if (fragmentClassName.equals(SettingsFragment.class.getName())) {
            setTitle(getString(R.string.settings_fragment_title));
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }
}
