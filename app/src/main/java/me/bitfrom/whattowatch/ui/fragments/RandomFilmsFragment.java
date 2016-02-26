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
import butterknife.BindString;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.fragments.presenters.RandomFilmsPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.RandomFilmsMvpView;
import me.bitfrom.whattowatch.ui.recyclerview.EmptyRecyclerView;
import me.bitfrom.whattowatch.ui.recyclerview.FilmsAdapter;
import me.bitfrom.whattowatch.ui.recyclerview.RecyclerItemClickListener;
import me.bitfrom.whattowatch.data.IdTransfer;
import me.bitfrom.whattowatch.utils.MessageHandlerUtility;


public class RandomFilmsFragment extends BaseFragment implements RandomFilmsMvpView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    protected RandomFilmsPresenter mRandomFilmsPresenter;
    @Inject
    protected FilmsAdapter mFilmsAdapter;

    @Bind(R.id.random_films_root_layout)
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.randrom_films_fragment, container, false);

        getFragmentComponent((MainActivity) getActivity()).inject(this);
        ButterKnife.bind(this, rootView);

        mRandomFilmsPresenter.attachView(this);

        initRecyclerView();
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRandomFilmsPresenter.getFilms();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRandomFilmsPresenter.detachView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mIdTransfer = (IdTransfer) getActivity();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRefresh() {
        loadNewFilms(true);
    }

    @Override
    public void showFilmsList(List<FilmModel> films) {
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
        MessageHandlerUtility.showMessage(mRootLayout,
                getString(R.string.error_connection_unavailable),
                Snackbar.LENGTH_LONG);
    }

    @Override
    public void loadNewFilms(boolean pullToRefresh) {
        mRandomFilmsPresenter.loadFilms(pullToRefresh);
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
