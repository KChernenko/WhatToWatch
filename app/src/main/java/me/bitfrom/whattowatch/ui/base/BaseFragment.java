package me.bitfrom.whattowatch.ui.base;

import android.app.Fragment;
import android.os.Bundle;

import com.squareup.leakcanary.RefWatcher;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.injection.component.FragmentComponent;
import me.bitfrom.whattowatch.injection.module.FragmentModule;

public class BaseFragment extends Fragment {

    private FragmentComponent fragmentComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WWApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @SuppressWarnings("deprecation")
    public FragmentComponent getFragmentComponent(BaseActivity activity) {
        if (fragmentComponent == null) {
            fragmentComponent = activity.getActivityComponent()
                    .addFragmentComponent(new FragmentModule(this));
        }

        return fragmentComponent;
    }

    public void releaseFragmentComponent() {
        fragmentComponent = null;
    }
}