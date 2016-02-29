package me.bitfrom.whattowatch.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.leakcanary.RefWatcher;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.injection.component.ActivityComponent;
import me.bitfrom.whattowatch.injection.component.DaggerActivityComponent;
import me.bitfrom.whattowatch.injection.module.ActivityModule;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(WWApplication.get(this).getComponent())
                    .build();
        }

        return mActivityComponent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WWApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
