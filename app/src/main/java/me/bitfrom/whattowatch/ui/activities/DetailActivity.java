package me.bitfrom.whattowatch.ui.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.core.image.ImageDownloader;
import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.ui.activities.presenters.DetailPresenter;
import me.bitfrom.whattowatch.ui.activities.views.DetailMvpView;
import me.bitfrom.whattowatch.ui.base.BaseActivity;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import me.bitfrom.whattowatch.utils.OnSwipeTouchListener;
import me.bitfrom.whattowatch.utils.ScrollManager;
import timber.log.Timber;

import static me.bitfrom.whattowatch.core.image.ImageLoaderInteractor.Flag;

@SuppressLint("NewApi")
public class DetailActivity extends BaseActivity implements DetailMvpView {

    @Inject
    protected DetailPresenter mDetailPresenter;
    @Inject
    protected ImageDownloader mImageLoader;
    @Inject
    protected ScrollManager mScrollManager;

    @Bind(R.id.detail_root_layout)
    protected CoordinatorLayout mRootLayout;
    @Bind(R.id.detail_toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.detail_scroll_view)
    protected NestedScrollView mScrollView;
    @Bind(R.id.poster)
    protected ImageView mPosterView;
    @Bind(R.id.title)
    protected TextView mTitleView;
    @Bind(R.id.country)
    protected TextView mCountriesView;
    @Bind(R.id.release_year)
    protected TextView mYearView;
    @Bind(R.id.runtime)
    protected TextView mRuntimeView;
    @Bind(R.id.rating)
    protected TextView mRatingView;
    @Bind(R.id.genres)
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

    @BindString(R.string.successfully_added_to_fav)
    protected String mSuccessfullyAddedToFav;
    @BindString(R.string.deleted_from_fav)
    protected String mAlreadyInFav;

    private String mFilmId;
    private Explode mExplode;
    private Transition.TransitionListener mTransitionListener;
    private OnSwipeTouchListener mSwipeTouchListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.detail_activity);

        ButterKnife.bind(this);
        mDetailPresenter.attachView(this);

        mFilmId = getIntent().getStringExtra(ConstantsManager.POSITION_ID_KEY);

        initActionBar();
        setupWindowAnimations();
        initSwipeListener();

        mScrollManager.hideViewInScrollView(mScrollView, mBtnAction, ScrollManager.Direction.DOWN);

        mDetailPresenter.getFilm(mFilmId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        mSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onDestroy() {
        mBtnSaveToFav.setOnClickListener(null);
        mBtnShare.setOnClickListener(null);
        mIMDBLink.setOnClickListener(null);
        mRootLayout.setOnTouchListener(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().removeListener(mTransitionListener);
            getWindow().getExitTransition().removeListener(mTransitionListener);
            mExplode.removeListener(mTransitionListener);
        }
        if (mDetailPresenter != null) mDetailPresenter.detachView();
        super.onDestroy();
        removeActivityFromTransitionManager(this);
    }

    @OnClick(R.id.action_share)
    public void btnShareClicked() {
        mDetailPresenter.shareWithFriends();
    }

    @OnClick(R.id.action_save_fav)
    public void btnFavoriteClicked() {
        mDetailPresenter.updateFavorites(mFilmId);
    }

    @OnClick(R.id.imdb_link)
    public void btnGoToImdbClicked() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setNegativeButton(R.string.dialog_negative, (dialog, which) -> {
                    dialog.cancel();
                })
                .setPositiveButton(R.string.dialog_positive, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mDetailPresenter.getImdbLink()));
                    this.startActivity(intent);
                }).show();
    }

    @Override
    public void showFilmInfo(@NonNull Film film) {
        mImageLoader.loadImage(Flag.FULL_SIZE, film.urlPoster(), mPosterView);
        mTitleView.setText(film.title());
        mCountriesView.setText(film.countries());
        mYearView.setText(film.year());
        mRuntimeView.setText(film.runtime());
        mRatingView.setText(film.rating());
        mGenresView.setText(film.genres());
        mDirectorsView.setText(film.directors());
        mWritersView.setText(film.writers());
        mPlotView.setText(film.plot());

        setCollapsingToolbarLayout(film.title());
    }

    @Override
    public void showUnknownError() {
        Snackbar.make(mScrollView,
                getString(R.string.error_list_empty), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddedToFavorites() {
        Snackbar.make(mScrollView, mSuccessfullyAddedToFav, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_fav, v -> {
                    mDetailPresenter.updateFavorites(mFilmId);
                }).show();
    }

    @Override
    public void showRemovedFromFavorites() {
        Snackbar.make(mScrollView, mAlreadyInFav, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_fav, v -> {
                    mDetailPresenter.updateFavorites(mFilmId);
                }).show();
    }

    @Override
    public void shareWithFriends(String shareInfo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.share_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextInputLayout til = (TextInputLayout) dialogView.findViewById(R.id.input_layout_comment);
        final EditText edt = (EditText) dialogView.findViewById(R.id.user_comment);

        dialogBuilder.setPositiveButton(R.string.share_action_next, (dialog, whichButton) -> {
            if (edt.getText().toString().length() >= ConstantsManager.SHARE_COMMENT_SIZE) {
                til.setError(getString(R.string.share_action_error_long_comment));
            } else {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, edt.getText().toString() + shareInfo);

                this.startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_to)));
            }
        });
        dialogBuilder.setNegativeButton(R.string.share_action_cancel, (dialog, whichButton) -> {
            dialog.cancel();
        }).create().show();
    }

    private void initActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Drawable navIcon = mToolbar.getNavigationIcon();
            if (navIcon != null) navIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mExplode = new Explode();
            mExplode.setDuration(ConstantsManager.TRANSITION_DURATION);
            iniTransitionListener();
            mExplode.addListener(mTransitionListener);
            getWindow().setEnterTransition(mExplode);
            getWindow().setExitTransition(mExplode);
        }
    }

    private void setCollapsingToolbarLayout(@NonNull String title) {
        mCollapsingToolbarLayout.setTitle(title);
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    }

    private void iniTransitionListener() {
        mTransitionListener = new Transition.TransitionListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTransitionStart(Transition transition) {
                Timber.d("onTransitionStart() was called!");
                transition.removeListener(this);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTransitionEnd(Transition transition) {
                Timber.d("onTransitionEnd() was called!");
                getWindow().getEnterTransition().removeListener(this);
                getWindow().getExitTransition().removeListener(this);
                transition.removeListener(this);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTransitionCancel(Transition transition) {
                Timber.d("onTransitionCancel() was called!");
                getWindow().getEnterTransition().removeListener(this);
                getWindow().getExitTransition().removeListener(this);
                transition.removeListener(this);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTransitionPause(Transition transition) {
                Timber.d("onTransitionPause() was called!");
                getWindow().getEnterTransition().removeListener(this);
                getWindow().getExitTransition().removeListener(this);
                transition.removeListener(this);
            }

            @Override
            public void onTransitionResume(Transition transition) {
                Timber.d("onTransitionResume() was called!");
                transition.removeListener(this);
            }
        };
    }

    private void initSwipeListener() {
        mSwipeTouchListener = new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                onBackPressed();
            }
        };

        mRootLayout.setOnTouchListener(mSwipeTouchListener);
    }
}