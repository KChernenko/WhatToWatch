package me.bitfrom.whattowatch.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.bitfrom.whattowatch.utils.ConstantsManager;

public class SliderFragment extends Fragment {

    private int layoutResId;

    public SliderFragment() {}

    public static SliderFragment newInstance(int layoutResId) {
        SliderFragment sampleSlide = new SliderFragment();

        Bundle args = new Bundle();
        args.putInt(ConstantsManager.ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ConstantsManager.ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ConstantsManager.ARG_LAYOUT_RES_ID);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutResId, container, false);
    }
}