package me.bitfrom.whattowatch.ui.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

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

    protected void removeActivityFromTransitionManager(Activity activity) {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        Class transitionManagerClass = TransitionManager.class;
        try {
            Field runningTransitionsField = transitionManagerClass.getDeclaredField("sRunningTransitions");
            runningTransitionsField.setAccessible(true);
            //noinspection unchecked
            ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> runningTransitions
                    = (ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>>)
                    runningTransitionsField.get(transitionManagerClass);
            if (runningTransitions.get() == null || runningTransitions.get().get() == null) {
                return;
            }
            ArrayMap map = runningTransitions.get().get();
            View decorView = activity.getWindow().getDecorView();
            if (map.containsKey(decorView)) {
                map.remove(decorView);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
