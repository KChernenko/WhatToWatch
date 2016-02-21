package me.bitfrom.whattowatch.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.ui.recyclerview.EmptyRecyclerView;
import me.bitfrom.whattowatch.utils.UriTransfer;


public class FavoritesFragment extends Fragment {

    @Bind(R.id.favorite_list)
    EmptyRecyclerView mRecyclerView;

    @Bind(R.id.favorite_list_empty)
    TextView mEmptyView;


    private UriTransfer uriTransfer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        uriTransfer = (UriTransfer) getActivity();

        super.onActivityCreated(savedInstanceState);
    }
}
