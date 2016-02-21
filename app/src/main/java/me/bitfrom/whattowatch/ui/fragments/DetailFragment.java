package me.bitfrom.whattowatch.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.data.image.ImageLoader;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.fragments.presenters.DetailPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.DetailMvpView;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import me.bitfrom.whattowatch.utils.MessageHandlerUtility;
import me.bitfrom.whattowatch.utils.ScrollManager;

import static me.bitfrom.whattowatch.data.image.ImageLoaderInteractor.Flag;


public class DetailFragment extends BaseFragment implements DetailMvpView {

    @Inject
    protected DetailPresenter mDetailPresenter;
    @Inject
    protected ImageLoader mImageLoader;
    @Inject
    protected ScrollManager mScrollManager;

    private String mImdbId;

    @Bind(R.id.detail_scroll_view)
    protected NestedScrollView mScrollView;
    @Bind(R.id.poster)
    protected ImageView mPosterView;
    @Bind(R.id.cv_title)
    protected TextView mTitleView;
    @Bind(R.id.country)
    protected TextView mCountriesView;
    @Bind(R.id.release_year)
    protected TextView mYearView;
    @Bind(R.id.runtime)
    protected TextView mRuntimeView;
    @Bind(R.id.rating)
    protected TextView mRatingView;
    @Bind(R.id.genre)
    protected TextView mGenresView;
    @Bind(R.id.director)
    protected TextView mDirectorsView;
    @Bind(R.id.writers)
    protected TextView mWritersView;
    @Bind(R.id.plot)
    protected TextView mPlotView;

    @Bind(R.id.multiple_actions)
    protected FloatingActionsMenu mBtnAction;
    @Bind(R.id.action_save_fav)
    protected FloatingActionButton mBtnSaveToFav;
    @Bind(R.id.action_share)
    protected FloatingActionButton mBtnShare;
    @Bind(R.id.imdb_link)
    protected FloatingActionButton mIMDBLink;

    @BindString(R.string.app_name)
    protected String mAppNameString;
    @BindString(R.string.share_action_awesome_intro)
    protected String mShareActionIntro;
    @BindString(R.string.share_action_imdb_rating)
    protected String mShareActionRating;
    @BindString(R.string.share_action_director)
    protected String mShareActionDirector;
    @BindString(R.string.app_hash_tag)
    protected String mShareHashTag;
    @BindString(R.string.successfully_added_to_fav)
    protected String mSuccessfullyAddedToFav;
    @BindString(R.string.deleted_from_fav)
    protected String mAlreadyInFav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        getFragmentComponent((MainActivity) getActivity()).inject(this);
        ButterKnife.bind(this, rootView);

        mDetailPresenter.attachView(this);

        Bundle extras = getArguments();
        if (extras != null) {
            mImdbId = extras.getString(ConstantsManager.POSITION_ID_KEY);
        }

        mDetailPresenter.getFilm(mImdbId);

        mScrollManager.hideViewInScrollView(mScrollView, mBtnAction, ScrollManager.Direction.DOWN);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mDetailPresenter.detachView();
    }

    @Override
    public void showFilmInfo(FilmModel film) {
        mImageLoader.loadImage(Flag.FULL_SIZE, film.urlPoster, mPosterView);
        mTitleView.setText(film.title);
        mCountriesView.setText(film.countries);
        mYearView.setText(film.year);
        mRuntimeView.setText(film.runtime);
        mRatingView.setText(film.rating);
        mGenresView.setText(film.genres);
        mDirectorsView.setText(film.directors);
        mWritersView.setText(film.writers);
        mPlotView.setText(film.plot);
    }

    @Override
    public void showUnknownError() {
        MessageHandlerUtility.showMessage(mScrollView,
                getString(R.string.error_unknown), Snackbar.LENGTH_LONG);
    }

    @Override
    public void addToFavorites() {

    }

    @Override
    public void shareWithFriends() {

    }

    @Override
    public void openImdbLink() {

    }
}
