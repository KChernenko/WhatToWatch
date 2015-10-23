package me.bitfrom.whattowatch.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.domain.contracts.ImageDownloadInteractor;
import me.bitfrom.whattowatch.domain.contracts.IpositionId;
import me.bitfrom.whattowatch.domain.weapons.ImageDownloadWeapon;
import me.bitfrom.whattowatch.utils.ScrollManager;
import me.bitfrom.whattowatch.utils.ShareUtility;
import rx.Subscription;

import static me.bitfrom.whattowatch.data.FilmsContract.FilmsEntry;

/**
 * Created by Constantine with love.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        IpositionId {

    @Bind(R.id.detail_scroll_view)
    ScrollView mScrollView;
    @Bind(R.id.poster)
    ImageView mPosterView;
    @Bind(R.id.cv_title)
    TextView mTitleView;
    @Bind(R.id.country)
    TextView mCountriesView;
    @Bind(R.id.release_year)
    TextView mYearView;
    @Bind(R.id.runtime)
    TextView mRuntimeView;
    @Bind(R.id.rating)
    TextView mRatingView;
    @Bind(R.id.genre)
    TextView mGenresView;
    @Bind(R.id.director)
    TextView mDirectorsView;
    @Bind(R.id.writers)
    TextView mWritersView;
    @Bind(R.id.plot)
    TextView mPlotView;

    @Bind(R.id.multiple_actions)
    FloatingActionsMenu mBtnAction;
    @Bind(R.id.action_save_fav)
    FloatingActionButton mBtnSaveToFav;
    @Bind(R.id.action_share)
    FloatingActionButton mBtnShare;

    @BindString(R.string.app_name)
    String mAppNameString;
    @BindString(R.string.share_action_awesome_intro)
    String mShareActionIntro;
    @BindString(R.string.share_action_imdb_rating)
    String mShareActionRating;
    @BindString(R.string.share_action_director)
    String mShareActionDirector;
    @BindString(R.string.app_hash_tag)
    String mShareHashTag;

    private Uri mUri;

    private static final int DETAIL_LOADER = 1;

    private static final String[] DETAIL_COLUMNS = {
            FilmsEntry.TABLE_NAME + "." + FilmsEntry._ID,
            FilmsEntry.COLUMN_URL_POSTER,
            FilmsEntry.COLUMN_TITLE,
            FilmsEntry.COLUMN_COUNTRIES,
            FilmsEntry.COLUMN_YEAR,
            FilmsEntry.COLUMN_RUNTIME,
            FilmsEntry.COLUMN_RATING,
            FilmsEntry.COLUMN_GENRES,
            FilmsEntry.COLUMN_DIRECTORS,
            FilmsEntry.COLUMN_WRITERS,
            FilmsEntry.COLUMN_PLOT,
            FilmsEntry.COLUMN_URL_IMDB
    };

    private ImageDownloadInteractor mImageWeapon;

    private ActionBar actionBar;


    private Subscription subscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle extras = getArguments();

        if (extras != null) {
            mUri = Uri.parse(extras.getString(ID_KEY));
        }

        ButterKnife.bind(this, rootView);

        setFabsIcons();

        mImageWeapon = new ImageDownloadWeapon();

        ScrollManager manager = new ScrollManager();
        manager.hideViewInScrollView(mScrollView, mBtnAction, ScrollManager.Direction.DOWN);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this).onContentChanged();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (actionBar != null) {
            actionBar.setTitle(mAppNameString);
        }
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
            String posterUrl = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_URL_POSTER));
            String title = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_TITLE));
            String country = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_COUNTRIES));
            String year = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_YEAR));
            String runtime = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_RUNTIME));
            String rating = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_RATING));
            String genres = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_GENRES));
            String director = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_DIRECTORS));
            String writer = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_WRITERS));
            String plot = data.getString(data.getColumnIndex(FilmsEntry.COLUMN_PLOT));

            mImageWeapon.loadPoster(ImageDownloadWeapon.FLAG.FULL_SIZE,
                    posterUrl, mPosterView);

            mTitleView.setText(title);
            mCountriesView.setText(country);
            mYearView.setText(year);
            mRuntimeView.setText(runtime);
            mRatingView.setText(rating);
            mGenresView.setText(genres);
            mDirectorsView.setText(director);
            mWritersView.setText(writer);
            mPlotView.setText(plot);

            share(title, rating, director, genres);
        }
    }

    private void setFabsIcons() {
        mBtnSaveToFav.setIcon(R.drawable.ic_star_white_24dp);
        mBtnShare.setIcon(R.drawable.ic_share_white_24dp);
    }

    private void share(String title, String rating, String director, String genres) {

        final StringBuilder sharedInfo = new StringBuilder(mShareActionIntro + " «" + title + "»" + "\n" +
                mShareActionRating + " " + rating + ".\n" +
                mShareActionDirector + " " + director + "\n" + genres + "\n");

        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtility.getShareActions(getActivity(), sharedInfo + mShareHashTag)
                        .title(R.string.share_to).show();
            }
        });
    }
}
