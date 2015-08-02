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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.domain.contracts.IpositionId;
import me.bitfrom.whattowatch.utils.BottomSheetHelper;

import static me.bitfrom.whattowatch.data.MoviesContract.*;

/**
 * Created by Constantine with love.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, IpositionId {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID,
            MoviesEntry.COLUMN_URL_POSTER,
            MoviesEntry.COLUMN_TITLE,
            MoviesEntry.COLUMN_COUNTRIES,
            MoviesEntry.COLUMN_YEAR,
            MoviesEntry.COLUMN_RUNTIME,
            MoviesEntry.COLUMN_RATING,
            MoviesEntry.COLUMN_GENRES,
            MoviesEntry.COLUMN_DIRECTORS,
            MoviesEntry.COLUMN_WRITERS,
            MoviesEntry.COLUMN_PLOT,
            MoviesEntry.COLUMN_URL_IMDB
    };

//    private static final String SHARE_HASHTAG = " #WhatToWatch";
//    private ShareActionProvider mShareActionProvider;
//    private String mMovieShareInfo;

    private CoordinatorLayout mCoordinatorLayout;

    private ImageView mPosterView;
    private TextView mTitleView;
    private TextView mCountriesView;
    private TextView mYearView;
    private TextView mRuntimeView;
    private TextView mRatingView;
    private TextView mGenresView;
    private TextView mDirectorsView;
    private TextView mWritersView;
    private TextView mPlotView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            mUri = Uri.parse(extras.getString(ID_KEY));
        }

        mCoordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.detail_container);
        mPosterView = (ImageView) rootView.findViewById(R.id.poster);
        mTitleView = (TextView) rootView.findViewById(R.id.tv_title);
        mCountriesView = (TextView) rootView.findViewById(R.id.country);
        mYearView = (TextView) rootView.findViewById(R.id.release_year);
        mRuntimeView = (TextView) rootView.findViewById(R.id.runtime);
        mRatingView = (TextView) rootView.findViewById(R.id.rating);
        mGenresView = (TextView) rootView.findViewById(R.id.genre);
        mDirectorsView = (TextView) rootView.findViewById(R.id.director);
        mWritersView = (TextView) rootView.findViewById(R.id.writers);
        mPlotView = (TextView) rootView.findViewById(R.id.plot);

        FloatingActionButton btnSaveToFav = (FloatingActionButton) rootView.findViewById(R.id.action_save_fav);
        btnSaveToFav.setIcon(R.drawable.ic_star_white_24dp);

        FloatingActionButton btnShare = (FloatingActionButton) rootView.findViewById(R.id.action_share);
        btnShare.setIcon(R.drawable.ic_share_white_24dp);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this).onContentChanged();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        getLoaderManager().destroyLoader(DETAIL_LOADER);
        super.onDestroy();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loadMovieInfo(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void loadMovieInfo(Cursor data) {
        if (data != null && data.moveToFirst()) {
            String posterUrl = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_URL_POSTER));
            String title = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_TITLE));
            String country = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_COUNTRIES));
            String year = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_YEAR));
            String runtime = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_RUNTIME));
            String rating = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_RATING));
            String genres = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_GENRES));
            String director = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_DIRECTORS));
            String writer = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_WRITERS));
            String plot = data.getString(data.getColumnIndex(MoviesEntry.COLUMN_PLOT));

            Picasso.with(getActivity())
                    .load(posterUrl)
                    .placeholder(R.drawable.progress_animation)
                    .resize(205, 310)
                    .centerCrop()
                    .into(mPosterView);
            mTitleView.setText(title);
            mCountriesView.setText(country);
            mYearView.setText(year);
            mRuntimeView.setText(runtime);
            mRatingView.setText(rating);
            mGenresView.setText(genres);
            mDirectorsView.setText(director);
            mWritersView.setText(writer);
            mPlotView.setText(plot);

            //enableShareActionProvider(title, rating, director, genres);
        }
    }

//    private void enableShareActionProvider(String title, String rating, String director, String genres) {
//        mMovieShareInfo = getString(R.string.share_action_awesome_intro) + " «" + title + "»" + "\n" +
//                getString(R.string.share_action_imdb_rating) + " " + rating + ".\n" +
//                getString(R.string.share_action_director) + " " + director + "\n" + genres + "\n";
//
//
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(createShareMovieIntent());
//        }
//    }


}
