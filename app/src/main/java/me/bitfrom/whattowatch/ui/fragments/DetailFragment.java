package me.bitfrom.whattowatch.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import me.bitfrom.whattowatch.data.image.ImageDownloader;
import me.bitfrom.whattowatch.data.model.Film;
import me.bitfrom.whattowatch.ui.activity.MainActivity;
import me.bitfrom.whattowatch.ui.base.BaseFragment;
import me.bitfrom.whattowatch.ui.fragments.presenters.DetailPresenter;
import me.bitfrom.whattowatch.ui.fragments.views.DetailMvpView;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import me.bitfrom.whattowatch.utils.ScrollManager;

import static me.bitfrom.whattowatch.data.image.ImageLoaderInteractor.Flag;


public class DetailFragment extends BaseFragment implements DetailMvpView {

    @Inject
    protected DetailPresenter mDetailPresenter;
    @Inject
    protected ImageDownloader mImageLoader;
    @Inject
    protected ScrollManager mScrollManager;

    private String mFilmId;

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
            mFilmId = extras.getString(ConstantsManager.POSITION_ID_KEY);
        }

        mDetailPresenter.getFilm(mFilmId);

        mScrollManager.hideViewInScrollView(mScrollView, mBtnAction, ScrollManager.Direction.DOWN);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mBtnSaveToFav.setOnClickListener(null);
        mBtnShare.setOnClickListener(null);
        mIMDBLink.setOnClickListener(null);
        if (mDetailPresenter != null) mDetailPresenter.detachView();
    }

    @Override
    public void showFilmInfo(Film film) {
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
        Snackbar.make(mScrollView,
                getString(R.string.error_unknown), Snackbar.LENGTH_LONG).show();
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
         new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setNegativeButton(R.string.dialog_negative, (dialog, which) -> {
                    dialog.cancel();
                })
                .setPositiveButton(R.string.dialog_positive, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mDetailPresenter.getImdbLink()));
                    getActivity().startActivity(intent);
                }).show();
    }

    @Override
    public void showAddedToFavorites() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Snackbar.make(mScrollView, mSuccessfullyAddedToFav, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo_fav, v -> {
                        mDetailPresenter.updateFavorites(mFilmId);
                    }).show();
        } else {
            Snackbar.make(mScrollView, mSuccessfullyAddedToFav, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showRemovedFromFavorites() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Snackbar.make(mScrollView, mAlreadyInFav, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo_fav, v -> {
                        mDetailPresenter.updateFavorites(mFilmId);
                    }).show();
        } else {
            Snackbar.make(mScrollView, mAlreadyInFav, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void shareWithFriends(String shareInfo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
}
