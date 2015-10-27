package me.bitfrom.whattowatch.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.adapter.FilmsRecyclerAdapter;
import me.bitfrom.whattowatch.adapter.listener.RecyclerItemClickListener;
import me.bitfrom.whattowatch.domain.contracts.FavoriteConstants;
import me.bitfrom.whattowatch.domain.contracts.UriTransfer;
import me.bitfrom.whattowatch.domain.weapons.LoadRandomFilmsWeapon;
import me.bitfrom.whattowatch.utils.EmptyRecyclerView;
import me.bitfrom.whattowatch.utils.MessageHandlerUtility;
import me.bitfrom.whattowatch.utils.NetworkStateChecker;
import me.bitfrom.whattowatch.utils.bus.ConnectionUnsuccessfulEvent;
import me.bitfrom.whattowatch.utils.bus.RestErrorEvent;
import me.bitfrom.whattowatch.utils.bus.RestSuccessfulEvent;

import static me.bitfrom.whattowatch.data.FilmsContract.FilmsEntry;

/**
 * Created by Constantine with love.
 */
public class RandomFilmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.list_of_films)
    EmptyRecyclerView mRecyclerView;

    @Bind(R.id.films_list_empty)
    TextView mEmptyView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ActionBar actionBar;

    private FilmsRecyclerAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String[] CARDS_PROJECTION = {
            FilmsEntry._ID,
            FilmsEntry.COLUMN_URL_POSTER,
            FilmsEntry.COLUMN_TITLE,
            FilmsEntry.COLUMN_DIRECTORS,
            FilmsEntry.COLUMN_GENRES,
            FilmsEntry.COLUMN_RATING,
            FilmsEntry.COLUMN_YEAR
    };

    private static final int RANDOM_FILMS_LOADER = 0;

    private UriTransfer uriTransfer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.randrom_films_fragment, container, false);

        ButterKnife.bind(this, rootView);

        initRecyclerView();
        initSwipeToRefresh();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(RANDOM_FILMS_LOADER, null, this);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        uriTransfer = (UriTransfer) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (actionBar != null) actionBar.setTitle(R.string.app_name);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        getLoaderManager().destroyLoader(RANDOM_FILMS_LOADER);
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = FilmsEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                baseUri,
                CARDS_PROJECTION,
                FilmsEntry.COLUMN_FAVORITE + " =?",
                new String[] {Integer.toString(FavoriteConstants.NOT_FAVORITE)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
        updateEmptyView();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

    /**
     * If RxJava Observer's onComplete() method was called, we'll show complete message.
     * **/
    public void onEventMainThread(RestSuccessfulEvent event) {
        MessageHandlerUtility.showMessage(mSwipeRefreshLayout, event.getMessage(),
                Snackbar.LENGTH_SHORT);
    }

    /**
     * If RxJava Observer's onError() method was called, we'll show error message.
     * **/
    public void onEventMainThread(RestErrorEvent event) {
        MessageHandlerUtility.showMessage(mSwipeRefreshLayout, event.getMessage(),
                Snackbar.LENGTH_LONG);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * If during the first app's launch connection unavailable, we'll show error message.
     * **/
    public void onEventMainThread(ConnectionUnsuccessfulEvent event) {
        updateEmptyView();
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMoviesAdapter = new FilmsRecyclerAdapter(getActivity(), null, 0);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Uri uri = FilmsEntry.buildFilmsUri(mMoviesAdapter.getItemId(position));
                        uriTransfer.sendUri(uri.toString());
                    }
                })
        );
    }

    /**
     * Update our list of films using swipe to refresh mechanism
     * **/
    private void initSwipeToRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStateChecker.isNetworkAvailable(WWApplication.getAppContext())) {
                    LoadRandomFilmsWeapon.loadFilms();
                } else {
                    NetworkStateChecker.showErrorMessage(mSwipeRefreshLayout);
                }
            }
        });
    }

    private void updateEmptyView() {
        if (mMoviesAdapter.getCount() == 0) {
            if (null != mEmptyView) {
                String message = getString(R.string.empty_films_list);
                if (! NetworkStateChecker.isNetworkAvailable(WWApplication.getAppContext())) {
                    message = getString(R.string.error_connection_unavailable);
                } else {
                    LoadRandomFilmsWeapon.loadFilms();
                }
                mEmptyView.setText(message);
            }
        }
    }
}
