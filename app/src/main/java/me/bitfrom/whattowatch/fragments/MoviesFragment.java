package me.bitfrom.whattowatch.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import me.bitfrom.whattowatch.DetailActivity;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.adapter.MoviesAdapter;
import me.bitfrom.whattowatch.adapter.listener.RecyclerItemClickListener;
import me.bitfrom.whattowatch.data.MoviesContract;
import me.bitfrom.whattowatch.utils.Utility;

import static me.bitfrom.whattowatch.data.MoviesContract.*;

/**
 * Created by Constantine with love.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private MoviesAdapter mMoviesAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_of_movies);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMoviesAdapter = new MoviesAdapter(getActivity(), null, 0);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.d(LOG_TAG, "POSITION: " + position);
                        Uri uri = MoviesEntry.buildMoviesUri((long) position+1);
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra(Utility.ID_KEY, uri.toString());
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = MoviesEntry.CONTENT_URI;

        return new CursorLoader(getActivity(), baseUri, CARDS_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }
}
