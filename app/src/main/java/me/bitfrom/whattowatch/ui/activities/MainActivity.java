package me.bitfrom.whattowatch.ui.activities;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.ui.activities.presenters.MainPresenter;
import me.bitfrom.whattowatch.ui.activities.views.MainMvpView;
import me.bitfrom.whattowatch.ui.base.BaseActivity;
import me.bitfrom.whattowatch.ui.fragments.BottomFilmsFragment;
import me.bitfrom.whattowatch.ui.fragments.ComingSoonFragment;
import me.bitfrom.whattowatch.ui.fragments.FavoritesFragment;
import me.bitfrom.whattowatch.ui.fragments.InCinemasFragment;
import me.bitfrom.whattowatch.ui.fragments.SettingsFragment;
import me.bitfrom.whattowatch.ui.fragments.TopFilmsFragment;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import timber.log.Timber;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainMvpView, Transition.TransitionListener {

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

    protected ActionBarDrawerToggle toggle;

    private Bundle mArgs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupDrawerLayout();
        setWindowAnimations();

        mMainPresenter.attachView(this);
        if (savedInstanceState == null) {
            mArgs = new Bundle();
            replaceFragment(new TopFilmsFragment());
        } else {
            mArgs = savedInstanceState;
        }

        mMainPresenter.initFirstSync();

        getFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment f = getFragmentManager().findFragmentById(R.id.main_container);
            if (f != null) {
                updateToolbarTitle(f);
            }
        });
    }

    @Override
    public void onDestroy() {
        drawerLayout.removeDrawerListener(toggle);
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
            case R.id.nav_in_cinemas:
                InCinemasFragment icf = new InCinemasFragment();
                mArgs.remove(ConstantsManager.POSITION_ID_KEY);
                replaceFragment(icf);
                break;
            case R.id.nav_coming_soon:
                ComingSoonFragment csf = new ComingSoonFragment();
                mArgs.remove(ConstantsManager.POSITION_ID_KEY);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showDataStartSyncing() {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout,
                getString(R.string.start_syncing_message), Snackbar.LENGTH_LONG);
        snackbar.setAction(getText(R.string.syncing_dismiss), v -> {
            snackbar.dismiss();
        });
        snackbar.show();
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

    @Override
    public void onTransitionStart(Transition transition) {
        Timber.d("onTransitionStart() was called!");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onTransitionEnd(Transition transition) {
        Timber.d("onTransitionEnd() was called!");
        getWindow().getEnterTransition().removeListener(this);
        getWindow().getExitTransition().removeListener(this);
        getWindow().getReenterTransition().removeListener(this);
        getWindow().getReturnTransition().removeListener(this);
        transition.removeListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onTransitionCancel(Transition transition) {
        Timber.d("onTransitionCancel() was called!");
        getWindow().getEnterTransition().removeListener(this);
        getWindow().getExitTransition().removeListener(this);
        getWindow().getReenterTransition().removeListener(this);
        getWindow().getReturnTransition().removeListener(this);
        transition.removeListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onTransitionPause(Transition transition) {
        Timber.d("onTransitionPause() was called!");
        getWindow().getEnterTransition().removeListener(this);
        getWindow().getExitTransition().removeListener(this);
        getWindow().getReenterTransition().removeListener(this);
        getWindow().getReturnTransition().removeListener(this);
        transition.removeListener(this);
    }

    @Override
    public void onTransitionResume(Transition transition) {
        Timber.d("onTransitionResume() was called!");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(ConstantsManager.TRANSITION_DURATION);
            explode.addListener(this);
            getWindow().setEnterTransition(explode);
            getWindow().setExitTransition(explode);
            getWindow().setReenterTransition(explode);
            getWindow().setReturnTransition(explode);
        }
    }
}
