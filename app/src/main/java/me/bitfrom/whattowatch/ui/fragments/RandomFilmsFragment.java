package me.bitfrom.whattowatch.ui.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.ui.activity.DetailActivity;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.adapter.FilmsRecyclerAdapter;
import me.bitfrom.whattowatch.adapter.listener.RecyclerItemClickListener;
import me.bitfrom.whattowatch.domain.contracts.IpositionId;
import me.bitfrom.whattowatch.utils.EmptyRecyclerView;
import me.bitfrom.whattowatch.utils.Utility;

import static me.bitfrom.whattowatch.data.FilmsContract.*;

/**
 * Created by Constantine with love.
 */
public class RandomFilmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, IpositionId {

    private static final String LOG_TAG = RandomFilmsFragment.class.getSimpleName();

    @Bind(R.id.list_of_films)
    EmptyRecyclerView mRecyclerView;

    @Bind(R.id.films_list_empty)
    TextView mEmptyView;

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

    private static final int CARDS_LOADER = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movies_list, container, false);

        ButterKnife.bind(this, rootView);

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = FilmsEntry.CONTENT_URI;

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


    private void updateEmptyView() {
        if (mMoviesAdapter.getCount() == 0) {
            TextView emptyView = (TextView) getView().findViewById(R.id.films_list_empty);
            if (null != emptyView) {
                String message = getString(R.string.empty_films_list);
                if (! Utility.isNetworkAvailable(getActivity())) {
                    message = getString(R.string.no_network_available);
                }
                emptyView.setText(message);
            }
        }
    }
}
