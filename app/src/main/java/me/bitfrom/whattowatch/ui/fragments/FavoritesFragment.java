package me.bitfrom.whattowatch.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.ui.activities.DetailActivity;
import me.bitfrom.whattowatch.ui.activities.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.fragments.presenters.FavoritesPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.FavoritesMvpView;
import me.bitfrom.whattowatch.ui.recyclerview.EmptyRecyclerView;
import me.bitfrom.whattowatch.ui.recyclerview.FilmsAdapter;
import me.bitfrom.whattowatch.ui.recyclerview.RecyclerItemClickListener;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class FavoritesFragment extends BaseFragment implements FavoritesMvpView {

    @Inject
    protected FavoritesPresenter mFavoritesPresenter;
    @Inject
    protected FilmsAdapter mFilmsAdapter;

    @Bind(R.id.favorite_root_layout)
    protected RelativeLayout mRootLayout;
    @Bind(R.id.favorite_list)
    protected EmptyRecyclerView mRecyclerView;
    @Bind(R.id.favorite_list_empty)
    protected TextView mEmptyView;

    @BindString(R.string.error_unknown)
    protected String mErrorUnknown;

    private RecyclerItemClickListener mRecyclerItemClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        getFragmentComponent((MainActivity) getActivity()).inject(this);
        ButterKnife.bind(this, rootView);

        initRecyclerView();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerItemClickListener = new RecyclerItemClickListener(getActivity(), (view, position) -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(ConstantsManager.POSITION_ID_KEY, mFilmsAdapter.getImdbIdByPosition(position));
            startActivity(intent);
        });
        mRecyclerView.addOnItemTouchListener(mRecyclerItemClickListener);
        mFavoritesPresenter.attachView(this);
        mFavoritesPresenter.getFavoriteFilms();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFavoritesPresenter != null) mFavoritesPresenter.detachView();
        if (mRecyclerView != null) mRecyclerView.removeOnItemTouchListener(mRecyclerItemClickListener);
    }

    @Override
    public void showListOfFavorites(List<Film> favoriteFilms) {
        mFilmsAdapter.setFilms(favoriteFilms);
        mFilmsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyList() {
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    public void showUnknownError() {
        Snackbar.make(mRootLayout,
                mErrorUnknown, Snackbar.LENGTH_LONG).show();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mFilmsAdapter);
    }
}
