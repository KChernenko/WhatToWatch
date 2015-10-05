package me.bitfrom.whattowatch.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.leakcanary.RefWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.domain.contracts.IpositionId;
import me.bitfrom.whattowatch.domain.contracts.UriTransfer;
import me.bitfrom.whattowatch.ui.fragments.DetailFragment;
import me.bitfrom.whattowatch.ui.fragments.RandomFilmsFragment;


public class MainActivity extends AppCompatActivity implements UriTransfer, IpositionId{

    private View.OnClickListener mOnClickListener;

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
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, new RandomFilmsFragment())
                    .commit();
        }

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };
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
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WWApplication.getRefWatcher();
        refWatcher.watch(this);
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
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void sendUri(String uri) {
        DetailFragment df = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ID_KEY, uri);
        df.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, df)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }
}
