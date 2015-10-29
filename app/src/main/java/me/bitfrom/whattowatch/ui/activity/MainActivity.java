package me.bitfrom.whattowatch.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.domain.contracts.IpositionId;
import me.bitfrom.whattowatch.domain.contracts.UriTransfer;
import me.bitfrom.whattowatch.ui.fragments.DetailFragment;
import me.bitfrom.whattowatch.ui.fragments.FavoritesFragment;
import me.bitfrom.whattowatch.ui.fragments.RandomFilmsFragment;
import me.bitfrom.whattowatch.ui.fragments.SettingsFragment;


public class MainActivity extends AppCompatActivity implements UriTransfer, IpositionId{

    @Bind(R.id.main_toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.header_avatar)
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initToolbar();
        setupDrawerLayout();

        avatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

        if(savedInstanceState == null) {
            replaceFragment(new RandomFilmsFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
     * Init the toolbar and enable action bar here.
     * **/
    private void initToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Setup navigation drawer here.
     * **/
    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_favorites:
                FavoritesFragment ff = new FavoritesFragment();
                replaceFragment(ff);
                break;
            case R.id.drawer_settings:
                SettingsFragment sf = new SettingsFragment();
                replaceFragment(sf);
                break;
            default:
                RandomFilmsFragment rf = new RandomFilmsFragment();
                replaceFragment(rf);
        }
        menuItem.setCheckable(true);
        drawerLayout.closeDrawers();
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
