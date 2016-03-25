package me.bitfrom.whattowatch.ui.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import me.bitfrom.whattowatch.ui.fragments.presenters.ComingSoonPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.ComingSoonMvpView;
import me.bitfrom.whattowatch.ui.recyclerview.FilmsAdapter;
import me.bitfrom.whattowatch.ui.recyclerview.RecyclerItemClickListener;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class ComingSoonFragment extends BaseFragment implements ComingSoonMvpView,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    protected ComingSoonPresenter mComingSoonPresenter;
    @Inject
    protected FilmsAdapter mFilmsAdapter;

    @Bind(R.id.films_root_layout)
    protected RelativeLayout mRootLayout;
    @Bind(R.id.list_of_films)
    protected RecyclerView mRecyclerView;
    @Bind(R.id.films_list_empty)
    protected TextView mEmptyView;
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindString(R.string.error_list_empty)
    protected String mErrorUnknown;

    private RecyclerItemClickListener mRecyclerItemClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.films_list_fragment, container, false);

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //noinspection unchecked
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity());
                ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
            } else {
                startActivity(intent);
            }
        });
        mRecyclerView.addOnItemTouchListener(mRecyclerItemClickListener);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mComingSoonPresenter.attachView(this);
        mComingSoonPresenter.getFilms();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mComingSoonPresenter != null) mComingSoonPresenter.detachView();
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setOnRefreshListener(null);
        if (mRecyclerView != null) mRecyclerView.removeOnItemTouchListener(mRecyclerItemClickListener);
    }

    @Override
    public void onRefresh() {
        loadNewFilms(true);
    }

    @Override
    public void showFilmsList(@NonNull List<Film> films) {
        mEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mFilmsAdapter.setFilms(films);
        mFilmsAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showListIsEmpty() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUnknownError() {
        Snackbar.make(mRootLayout, mErrorUnknown, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showInternetUnavailableError() {
        Snackbar.make(mRootLayout,
                getString(R.string.error_connection_unavailable),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loadNewFilms(boolean pullToRefresh) {
        mComingSoonPresenter.loadFilms(pullToRefresh);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        mSwipeRefreshLayout.setRefreshing(pullToRefresh);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFilmsAdapter);
    }
}
