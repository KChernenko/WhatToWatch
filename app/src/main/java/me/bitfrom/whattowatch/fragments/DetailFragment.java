package me.bitfrom.whattowatch.fragments;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.data.MoviesContract;
import me.bitfrom.whattowatch.utils.Utility;

import static me.bitfrom.whattowatch.data.MoviesContract.*;

/**
 * Created by Constantine with love.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String DETAIL_URI = "URI";
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


    private static final String SHARE_HASHTAG = " #WhatToWatch";
    private ShareActionProvider mShareActionProvider;
    private String mMovieShareInfo;

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

    private Loader<Cursor> mLoader;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mUri = Uri.parse(extras.getString(Utility.ID_KEY));
        }

        Log.d(LOG_TAG, mUri.toString());

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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
       // getActivity().getContentResolver().registerContentObserver(mUri, true, new MyObserver(new Handler()));
        getLoaderManager().initLoader(DETAIL_LOADER, null, this).onContentChanged();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);
        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mMovieShareInfo == null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
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
        updateData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "Data reseted!");
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovieShareInfo + SHARE_HASHTAG);

        return shareIntent;
    }

    private void updateData(Cursor data) {
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

            mMovieShareInfo = "Awesome movie «" + title + "»" + "\n" +
                    "which IMDB rating is " + rating + "\n" +
                    "And directed by " + director + "\n" + genres + "\n";

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }
        }

    }

}
