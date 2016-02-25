package me.bitfrom.whattowatch.ui.base;

import android.app.Fragment;
import android.os.Bundle;

import com.squareup.leakcanary.RefWatcher;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.injection.component.DaggerFragmentComponent;
import me.bitfrom.whattowatch.injection.component.FragmentComponent;
import me.bitfrom.whattowatch.injection.module.ActivityModule;
import me.bitfrom.whattowatch.injection.module.FragmentModule;
import me.bitfrom.whattowatch.ui.activity.MainActivity;

public class BaseFragment extends Fragment{

    private FragmentComponent mFragmentComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public FragmentComponent getFragmentComponent(MainActivity mainActivity) {
        if (mFragmentComponent == null) {
            mFragmentComponent = DaggerFragmentComponent.builder()
                    .activityModule(new ActivityModule(mainActivity))
                    .fragmentModule(new FragmentModule(this))
                    .applicationComponent(WWApplication.get(mainActivity).getComponent())
                    .build();
        }

        return mFragmentComponent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WWApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
