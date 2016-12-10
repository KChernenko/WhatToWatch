package me.bitfrom.whattowatch.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.leakcanary.RefWatcher;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.injection.component.ActivityComponent;
import me.bitfrom.whattowatch.injection.module.ActivityModule;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WWApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = WWApplication.getApplication()
                    .getComponent()
                    .addActivityComponent(new ActivityModule(this));
        }

        return activityComponent;
    }

    public void releaseActivityComponent() {
        activityComponent = null;
    }
}