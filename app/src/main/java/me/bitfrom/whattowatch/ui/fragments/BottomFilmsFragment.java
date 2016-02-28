package me.bitfrom.whattowatch.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import me.bitfrom.whattowatch.data.model.Film;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.fragments.presenters.BottomFilmsPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.BottomFilmsMvpView;
import me.bitfrom.whattowatch.ui.recyclerview.EmptyRecyclerView;
import me.bitfrom.whattowatch.ui.recyclerview.FilmsAdapter;
import me.bitfrom.whattowatch.ui.recyclerview.RecyclerItemClickListener;

public class BottomFilmsFragment extends BaseFragment implements BottomFilmsMvpView,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    protected BottomFilmsPresenter mBottomFilmsPresenter;
    @Inject
    protected FilmsAdapter mFilmsAdapter;

    @Bind(R.id.films_root_layout)
    protected RelativeLayout mRootLayout;
    @Bind(R.id.list_of_films)
    protected EmptyRecyclerView mRecyclerView;
    @Bind(R.id.films_list_empty)
    protected TextView mEmptyView;
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindString(R.string.error_unknown)
    protected String mErrorUnknown;

    private IdTransfer mIdTransfer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.films_list_fragment, container, false);

        getFragmentComponent((MainActivity) getActivity()).inject(this);
        ButterKnife.bind(this, rootView);

        mBottomFilmsPresenter.attachView(this);

        initRecyclerView();
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mBottomFilmsPresenter.getFilms();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mIdTransfer = (IdTransfer) getActivity();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBottomFilmsPresenter != null) mBottomFilmsPresenter.detachView();
    }

    @Override
    public void onRefresh() {
        loadNewFilms(true);
    }

    @Override
    public void showFilmsList(List<Film> films) {
        mFilmsAdapter.setFilms(films);
        mFilmsAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showUnknownError() {
        mEmptyView.setText(mErrorUnknown);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    public void showInternetUnavailableError() {
        Snackbar.make(mRootLayout,
                getString(R.string.error_connection_unavailable),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loadNewFilms(boolean pullToRefresh) {
        mBottomFilmsPresenter.loadFilms(pullToRefresh);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        mSwipeRefreshLayout.setRefreshing(pullToRefresh);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mFilmsAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), (view, position) -> {
                    if (mIdTransfer != null) mIdTransfer.sendFilmId(mFilmsAdapter.getImdbIdByPosition(position));
                })
        );
    }
}
