package me.bitfrom.whattowatch.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.domain.contracts.IpositionId;
import me.bitfrom.whattowatch.domain.contracts.UriTransfer;
import me.bitfrom.whattowatch.ui.base.BaseActivity;
import me.bitfrom.whattowatch.ui.fragments.DetailFragment;
import me.bitfrom.whattowatch.ui.fragments.FavoritesFragment;
import me.bitfrom.whattowatch.ui.fragments.RandomFilmsFragment;
import me.bitfrom.whattowatch.ui.fragments.SettingsFragment;
import me.bitfrom.whattowatch.utils.MessageHandlerUtility;


public class MainActivity extends BaseActivity implements UriTransfer, IpositionId,
        NavigationView.OnNavigationItemSelectedListener, MainMvpView {


    @Inject
    protected MainPresenter mMainPresenter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupDrawerLayout();

        mMainPresenter.attachView(this);
        if (savedInstanceState == null) {
            replaceFragment(new RandomFilmsFragment());
        }

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getFragmentManager().findFragmentById(R.id.main_container);
                if (f != null) {
                    updateToolbarTitle(f);
                }
            }
        });
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
                replaceFragment(af);
                break;
            case R.id.nav_trailers:
                MessageHandlerUtility.showMessage(coordinatorLayout,
                        getString(R.string.trailers_fragment_message),
                        Snackbar.LENGTH_LONG);
                break;
            case R.id.nav_settings:
                SettingsFragment sf = new SettingsFragment();
                replaceFragment(sf);
                break;
            default:
                RandomFilmsFragment rff = new RandomFilmsFragment();
                replaceFragment(rff);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void sendUri(String uri) {
        DetailFragment df = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ID_KEY, uri);
        df.setArguments(args);
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

        if (fragmentClassName.equals(RandomFilmsFragment.class.getName())) {
            setTitle(getString(R.string.random_films_fragment_title));
            navigationView.setCheckedItem(R.id.nav_random_films);
        } else if (fragmentClassName.equals(FavoritesFragment.class.getName())) {
            setTitle(getString(R.string.drawer_item_favorites));
            navigationView.setCheckedItem(R.id.nav_favorites);
        } else if (fragmentClassName.equals(SettingsFragment.class.getName())) {
            setTitle(getString(R.string.settings_fragment_title));
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }
}
