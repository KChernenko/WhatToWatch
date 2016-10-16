package me.bitfrom.whattowatch.ui.details;

import android.annotation.SuppressLint;
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

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.core.image.ImageDownloader;
import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.ui.base.BaseActivity;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import me.bitfrom.whattowatch.utils.OnSwipeTouchListener;
import me.bitfrom.whattowatch.utils.ScrollManager;

import static me.bitfrom.whattowatch.core.image.ImageLoaderInteractor.Flag;

@SuppressLint("NewApi")
public class DetailActivity extends BaseActivity implements DetailMvpView {

    @Inject
    protected DetailPresenter detailPresenter;
    @Inject
    protected ImageDownloader imageDownloader;
    @Inject
    protected ScrollManager scrollManager;

    @BindView(R.id.detail_root_layout)
    protected CoordinatorLayout rootLayout;
    @BindView(R.id.detail_toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.detail_scroll_view)
    protected NestedScrollView nestedScrollView;
    @BindView(R.id.poster)
    protected ImageView posterView;
    @BindView(R.id.title)
    protected TextView titleView;
    @BindView(R.id.country)
    protected TextView countriesView;
    @BindView(R.id.release_year)
    protected TextView yearView;
    @BindView(R.id.runtime)
    protected TextView runtimeView;
    @BindView(R.id.rating)
    protected TextView ratingView;
    @BindView(R.id.genres)
    protected TextView genresView;
    @BindView(R.id.director)
    protected TextView directorsView;
    @BindView(R.id.writers)
    protected TextView writesView;
    @BindView(R.id.plot)
    protected TextView plotView;

    @BindView(R.id.multiple_actions)
    protected FloatingActionsMenu btnAction;
    @BindView(R.id.action_save_fav)
    protected FloatingActionButton btnSaveToFavorites;
    @BindView(R.id.action_share)
    protected FloatingActionButton btnShare;
    @BindView(R.id.imdb_link)
    protected FloatingActionButton btnIMDBLink;

    @BindString(R.string.successfully_added_to_fav)
    protected String successfullyAddedToFavMsg;
    @BindString(R.string.deleted_from_fav)
    protected String alreadyInFavMsg;
    @BindString(R.string.unknown_error_msg)
    protected String unknownErrorMsg;
    @BindColor(android.R.color.transparent)
    protected int transparentColor;

    private OnSwipeTouchListener swipeTouchListener;

    private String filmId;

    //For sharing purposes
    private String title;
    private String rating;
    private String directors;
    private String genres;
    private String imdbLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.detail_activity);

        ButterKnife.bind(this);
        detailPresenter.attachView(this);

        filmId = getIntent().getStringExtra(ConstantsManager.POSITION_ID_KEY);

        initActionBar();
        initSwipeListener();

        scrollManager.hideViewInScrollView(nestedScrollView, btnAction, ScrollManager.Direction.DOWN);

        detailPresenter.getFilm(filmId);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        swipeTouchListener.getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onDestroy() {
        btnSaveToFavorites.setOnClickListener(null);
        btnShare.setOnClickListener(null);
        btnIMDBLink.setOnClickListener(null);
        rootLayout.setOnTouchListener(null);
        if (detailPresenter != null) detailPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_fade_in, R.anim.exit_push_out);
    }

    @OnClick(R.id.action_share)
    public void btnShareClicked() {
        String sharedInfo = " «" +
                title + "»" + "\n" +
                getString(R.string.share_action_imdb_rating) + " " + rating +
                ".\n" + getString(R.string.share_action_directors) + " " +
                directors + "\n" + genres + "\n" +
                getString(R.string.app_hash_tag);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.share_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextInputLayout til = (TextInputLayout) dialogView.findViewById(R.id.input_layout_comment);
        final EditText edt = (EditText) dialogView.findViewById(R.id.user_comment);

        dialogBuilder.setPositiveButton(R.string.share_action_next, (dialog, whichButton) -> {
            if (edt.getText().toString().length() >= ConstantsManager.SHARE_COMMENT_SIZE) {
                til.setError(getString(R.string.share_action_error_long_comment));
            } else {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, edt.getText().toString() + sharedInfo);

                this.startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_to)));
            }
        });
        dialogBuilder.setNegativeButton(R.string.share_action_cancel,
                (dialog, whichButton) -> dialog.cancel()).create().show();
    }

    @OnClick(R.id.action_save_fav)
    public void btnFavoriteClicked() {
        detailPresenter.updateFavorites(filmId);
    }

    @OnClick(R.id.imdb_link)
    public void btnGoToImdbClicked() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setNegativeButton(R.string.dialog_negative, (dialog, which) ->
                    dialog.cancel())
                .setPositiveButton(R.string.dialog_positive, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(imdbLink));
                    this.startActivity(intent);
                }).show();
    }

    @Override
    public void showFilmInfo(@NonNull FilmEntity film) {
        //noinspection ConstantConditions
        imageDownloader.loadImage(Flag.FULL_SIZE, film.posterUrl(), posterView);
        title = film.title();
        titleView.setText(title);
        countriesView.setText(film.countries());
        yearView.setText(film.year());
        runtimeView.setText(film.runtime());
        rating = film.rating();
        ratingView.setText(rating);
        genres = film.genres();
        genresView.setText(genres);
        directors = film.directors();
        directorsView.setText(directors);
        writesView.setText(film.writers());
        plotView.setText(film.plot());

        imdbLink = film.imbdUrl();

        setCollapsingToolbarLayout(film.title());
    }

    @Override
    public void showUnknownError() {
        Snackbar.make(nestedScrollView, unknownErrorMsg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddedToFavorites() {
        Snackbar.make(nestedScrollView, successfullyAddedToFavMsg, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_fav, v -> detailPresenter.updateFavorites(filmId)).show();
    }

    @Override
    public void showRemovedFromFavorites() {
        Snackbar.make(nestedScrollView, alreadyInFavMsg, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_fav, v -> detailPresenter.updateFavorites(filmId)).show();
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Drawable navIcon = toolbar.getNavigationIcon();
            if (navIcon != null) navIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    private void setCollapsingToolbarLayout(@NonNull String title) {
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleColor(transparentColor);
    }


    private void initSwipeListener() {
        swipeTouchListener = new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                onBackPressed();
            }
        };

        rootLayout.setOnTouchListener(swipeTouchListener);
    }
}