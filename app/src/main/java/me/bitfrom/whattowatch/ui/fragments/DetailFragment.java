package me.bitfrom.whattowatch.ui.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import me.bitfrom.whattowatch.utils.ScrollManager;


public class DetailFragment extends Fragment {


    @Bind(R.id.detail_scroll_view)
    NestedScrollView mScrollView;
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
    @Bind(R.id.imdb_link)
    FloatingActionButton mIMDBLink;

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
    @BindString(R.string.successfully_added_to_fav)
    String mSuccessfullyAddedToFav;
    @BindString(R.string.deleted_from_fav)
    String mAlreadyInFav;

    private Uri mUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle extras = getArguments();

        if (extras != null) {
            mUri = Uri.parse(extras.getString(ConstantsManager.POSITION_ID_KEY));
        }

        ButterKnife.bind(this, rootView);


        ScrollManager manager = new ScrollManager();
        manager.hideViewInScrollView(mScrollView, mBtnAction, ScrollManager.Direction.DOWN);

        return rootView;
    }
}
