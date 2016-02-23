package me.bitfrom.whattowatch.ui.fragments.presenters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.injection.ActivityContext;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.ui.fragments.views.DetailMvpView;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager mDataManager;
    private Context mContext;
    private Subscription mSubscription;

    private String mTitle;
    private String mRating;
    private String mDirectors;
    private String mGenres;
    private String mImdbLink;

    @Inject
    public DetailPresenter(DataManager dataManager, @ActivityContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(DetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        mContext = null;
    }

    public void getFilm(final String filmId) {
        checkViewAttached();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.getTopFilmById(filmId)
                .subscribeOn(Schedulers.newThread())
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilmModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showUnknownError();
                        Timber.d("Error occurred!", e);
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(FilmModel film) {
                        if (film == null) {
                            getMvpView().showUnknownError();
                        } else {
                            getMvpView().showFilmInfo(film);
                            //Init strings for sharing
                            initSharedInformation(film);
                        }
                    }
                });
    }

    public void shareWithFriends() {
        checkViewAttached();
        StringBuilder sharedInfo = new StringBuilder();
        sharedInfo.append(mContext.getString(R.string.share_action_awesome_intro)).append(" «")
                .append(mTitle).append("»").append("\n")
                .append(mContext.getString(R.string.share_action_imdb_rating)).append(" ").append(mRating)
                .append(".\n").append(mContext.getString(R.string.share_action_directors)).append(" ")
                .append(mDirectors).append("\n").append(mGenres).append("\n")
                .append(mContext.getString(R.string.app_hash_tag));

        getMvpView().shareWithFriends(sharedInfo.toString());
    }

    public void updateFavorites(final String filmId) {
        checkViewAttached();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.getTopFilmById(filmId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FilmModel>() {
                    @Override
                    public void call(FilmModel film) {
                        if (film.favorite.equals(String.valueOf(ConstantsManager.NOT_FAVORITE))) {
                            mDataManager.addToFavorite(filmId);
                            getMvpView().showAddedToFavorites();
                        } else {
                            mDataManager.removeFromFavorite(filmId);
                            getMvpView().showRemovedFromFavorites();
                        }
                    }
                });
    }

    public void openImdbWebsite() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(mImdbLink));
                        mContext.startActivity(intent);
                    }
                });
        getMvpView().imdbLinkDialog(alertDialog);
    }

    private void initSharedInformation(FilmModel film) {
        mTitle = film.title;
        mRating = film.rating;
        mDirectors = film.directors;
        mGenres = film.genres;
        mImdbLink = film.urlIMDB;
    }
}
