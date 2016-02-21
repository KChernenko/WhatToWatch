package me.bitfrom.whattowatch.ui.fragments;

import android.os.Bundle;
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
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.recyclerview.EmptyRecyclerView;
import me.bitfrom.whattowatch.ui.recyclerview.FilmsAdapter;
import me.bitfrom.whattowatch.utils.MessageHandlerUtility;
import me.bitfrom.whattowatch.utils.NetworkStateChecker;
import me.bitfrom.whattowatch.utils.IdTransfer;


public class RandomFilmsFragment extends BaseFragment implements RandomFilmsMvpView {

    @Bind(R.id.random_films_root_layout)
    protected RelativeLayout mRootLayout;
    @Bind(R.id.list_of_films)
    protected EmptyRecyclerView mRecyclerView;
    @Bind(R.id.films_list_empty)
    protected TextView mEmptyView;
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    protected RandomFilmsPresenter mRandomFilmsPresenter;
    @Inject
    protected FilmsAdapter mFilmsAdapter;

    private IdTransfer idTransfer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.randrom_films_fragment, container, false);

        getFragmentComponent((MainActivity) getActivity()).inject(this);
        mRandomFilmsPresenter.attachView(this);

        ButterKnife.bind(this, rootView);

        initRecyclerView();
        initSwipeToRefresh();

        mRandomFilmsPresenter.getFilms();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mRandomFilmsPresenter.detachView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        idTransfer = (IdTransfer) getActivity();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showFilmsList(List<FilmModel> films) {
        mFilmsAdapter.setFilms(films);
        mFilmsAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showFilmsListEmpty() {
        MessageHandlerUtility.showMessage(mRootLayout, "The list such empty((",
                Snackbar.LENGTH_LONG);
    }

    @Override
    public void showError() {
        MessageHandlerUtility.showMessage(mRootLayout, "Something bad had happened!",
                Snackbar.LENGTH_LONG);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mFilmsAdapter);
    }

    /**
     * Update our list of films using swipe to refresh mechanism
     **/
    private void initSwipeToRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStateChecker.isNetworkAvailable(getActivity())) {
                    mRandomFilmsPresenter.loadFilms();
                } else {
                    MessageHandlerUtility.showMessage(mRootLayout,
                            getString(R.string.error_connection_unavailable),
                            Snackbar.LENGTH_LONG);
                }
            }
        });
    }
}
