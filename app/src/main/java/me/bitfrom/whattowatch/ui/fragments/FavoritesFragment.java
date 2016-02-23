package me.bitfrom.whattowatch.ui.fragments;


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
import me.bitfrom.whattowatch.data.IdTransfer;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.fragments.presenters.FavoritesPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.FavoritesMvpView;
import me.bitfrom.whattowatch.ui.recyclerview.EmptyRecyclerView;
import me.bitfrom.whattowatch.ui.recyclerview.FilmsAdapter;
import me.bitfrom.whattowatch.ui.recyclerview.RecyclerItemClickListener;
import me.bitfrom.whattowatch.utils.MessageHandlerUtility;


public class FavoritesFragment extends BaseFragment implements FavoritesMvpView {

    @Inject
    protected FavoritesPresenter mFavoritesPresenter;
    @Inject
    protected FilmsAdapter mFilmsAdapter;

    private IdTransfer mIdTransfer;

    @Bind(R.id.favorite_root_layout)
    protected RelativeLayout mRootLayout;
    @Bind(R.id.favorite_list)
    protected EmptyRecyclerView mRecyclerView;
    @Bind(R.id.favorite_list_empty)
    protected TextView mEmptyView;

    @BindString(R.string.error_unknown)
    protected String mErrorUnknown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        getFragmentComponent((MainActivity) getActivity()).inject(this);
        ButterKnife.bind(this, rootView);

        mFavoritesPresenter.attachView(this);
        initRecyclerView();

        mFavoritesPresenter.getFavoriteFilms();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mIdTransfer = (IdTransfer) getActivity();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFavoritesPresenter.detachView();
    }

    @Override
    public void showListOfFavorites(List<FilmModel> favoriteFilms) {
        mFilmsAdapter.setFilms(favoriteFilms);
        mFilmsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyList() {
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    public void showUnknownError() {
        MessageHandlerUtility.showMessage(mRootLayout,
                mErrorUnknown, Snackbar.LENGTH_LONG);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mFilmsAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (mIdTransfer != null) mIdTransfer.sendFilmId(mFilmsAdapter.getImdbIdByPosition(position));
                    }
                })
        );
    }
}
