package me.bitfrom.whattowatch.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;

import de.greenrobot.event.EventBus;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.ui.fragments.RandomFilmsFragment;
import me.bitfrom.whattowatch.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.utils.ServerMessageEvent;


public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout mCoordinator;
    private View.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_container);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, new RandomFilmsFragment())
                    .commit();
        }

        FilmsSyncAdapter.initializeSyncAdapter(this);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Shows a snackbar if an event was triggered
     * @param event ServerMessageEvent
     * **/
    public void onEventMainThread(ServerMessageEvent event) {
        if (event != null) {
            Snackbar.make(mCoordinator, event.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_close_app, mOnClickListener)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        if (id == R.id.action_sync_now) {
            FilmsSyncAdapter.syncImmediately(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WWApplication.getRefWatcher();
        refWatcher.watch(this);
    }
}
