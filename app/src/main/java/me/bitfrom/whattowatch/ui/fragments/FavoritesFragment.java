package me.bitfrom.whattowatch.ui.fragments;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.adapter.FilmsRecyclerAdapter;
import me.bitfrom.whattowatch.adapter.listener.RecyclerItemClickListener;
import me.bitfrom.whattowatch.data.FilmsContract;
import me.bitfrom.whattowatch.domain.contracts.FavoriteConstants;
import me.bitfrom.whattowatch.domain.contracts.UriTransfer;
import me.bitfrom.whattowatch.utils.EmptyRecyclerView;


public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @Bind(R.id.favorite_list)
    EmptyRecyclerView mRecyclerView;

    @Bind(R.id.favorite_list_empty)
    TextView mEmptyView;

    private ActionBar actionBar;

    private FilmsRecyclerAdapter mFavoriteAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String[] CARDS_PROJECTION = {
            FilmsContract.FilmsEntry._ID,
            FilmsContract.FilmsEntry.COLUMN_URL_POSTER,
            FilmsContract.FilmsEntry.COLUMN_TITLE,
            FilmsContract.FilmsEntry.COLUMN_DIRECTORS,
            FilmsContract.FilmsEntry.COLUMN_GENRES,
            FilmsContract.FilmsEntry.COLUMN_RATING,
            FilmsContract.FilmsEntry.COLUMN_YEAR
    };

    private static final int FAVORITE_FILMS_LOADER = 4;

    private UriTransfer uriTransfer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, rootView);

        initRecyclerView();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVORITE_FILMS_LOADER, null, this);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        uriTransfer = (UriTransfer) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (actionBar != null) actionBar.setTitle(R.string.favorite_fragment_title);
    }

    @Override
    public void onDestroy() {
        getLoaderManager().destroyLoader(FAVORITE_FILMS_LOADER);
        super.onDestroy();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = FilmsContract.FilmsEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                baseUri,
                CARDS_PROJECTION,
                FilmsContract.FilmsEntry.COLUMN_FAVORITE + " =?",
                new String[] {Integer.toString(FavoriteConstants.FAVORITE)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavoriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFavoriteAdapter = new FilmsRecyclerAdapter(getActivity(), null, 0);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mFavoriteAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Uri uri = FilmsContract.FilmsEntry.buildFilmsUri(mFavoriteAdapter.getItemId(position));
                        uriTransfer.sendUri(uri.toString());
                    }
                })
        );
    }
}
