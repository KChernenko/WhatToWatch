package me.bitfrom.whattowatch.ui.base;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.squareup.leakcanary.RefWatcher;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.injection.component.FragmentComponent;
import me.bitfrom.whattowatch.injection.module.FragmentModule;

public class BasePreferenceFragment extends PreferenceFragment {

    private FragmentComponent fragmentComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    public FragmentComponent getFragmentComponent(BaseActivity activity) {
        if (fragmentComponent == null) {
            fragmentComponent = activity.getActivityComponent()
                    .addFragmentComponent(new FragmentModule(this));
        }

        return fragmentComponent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WWApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
