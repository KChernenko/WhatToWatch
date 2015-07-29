package me.bitfrom.whattowatch.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.bitfrom.whattowatch.ui.activity.DetailActivity;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.adapter.MoviesAdapter;
import me.bitfrom.whattowatch.adapter.listener.RecyclerItemClickListener;
import me.bitfrom.whattowatch.domain.contracts.IpositionId;
import me.bitfrom.whattowatch.utils.EmptyRecyclerView;
import me.bitfrom.whattowatch.utils.Utility;

import static me.bitfrom.whattowatch.data.MoviesContract.*;

/**
 * Created by Constantine with love.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, IpositionId {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private MoviesAdapter mMoviesAdapter;
    private EmptyRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView mEmptyView;

    private static final String[] CARDS_PROJECTION = {
            MoviesEntry._ID,
            MoviesEntry.COLUMN_URL_POSTER,
            MoviesEntry.COLUMN_TITLE,
            MoviesEntry.COLUMN_DIRECTORS,
            MoviesEntry.COLUMN_GENRES,
            MoviesEntry.COLUMN_RATING,
            MoviesEntry.COLUMN_YEAR
    };

    private static final int CARDS_LOADER = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movies_list, container, false);

        mRecyclerView = (EmptyRecyclerView) rootView.findViewById(R.id.list_of_movies);
        mEmptyView = (TextView) rootView.findViewById(R.id.movielist_empty);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMoviesAdapter = new MoviesAdapter(getActivity(), null, 0);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Uri uri = MoviesEntry.buildMoviesUri(mMoviesAdapter.getItemId(position));
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra(ID_KEY, uri.toString());
                        startActivity(intent);
                    }
                })
        );

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CARDS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        getLoaderManager().destroyLoader(CARDS_LOADER);
        super.onDestroy();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = MoviesEntry.CONTENT_URI;

        return new CursorLoader(getActivity(), baseUri, CARDS_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
        updateEmptyView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

//    public void onEventMainThread(ServerMessageEvent event) {
//        Log.d(LOG_TAG, "onEvent was triggered!");
//        if (event != null) {
//            serverIsDownMsg = event.getMessage();
//            //Log.d(LOG_TAG, "MSG: " + serverIsDownMsg);
//            //Toast.makeText(getActivity(), serverIsDownMsg, Toast.LENGTH_LONG).show();
//            //Snackbar.make(mCoordinator, "Had a snack at Snackbar", Snackbar.LENGTH_LONG)
//             //       .show();
//        }
//        serverIsAvailable = false;
//    }

    private void updateEmptyView() {
        if (mMoviesAdapter.getCount() == 0) {
            TextView emptyView = (TextView) getView().findViewById(R.id.movielist_empty);
            if (null != emptyView) {
                String message = getString(R.string.empty_movie_list);
                if (! Utility.isNetworkAvailable(getActivity())) {
                    message = getString(R.string.no_network_available);
                }
                emptyView.setText(message);
            }
        }
    }
}
