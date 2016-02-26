package me.bitfrom.whattowatch.ui.fragments.presenters;


import android.content.Context;

import javax.inject.Inject;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.data.DataManager;
import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.ui.fragments.views.DetailMvpView;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
    public DetailPresenter(DataManager dataManager, @ApplicationContext Context context) {
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
    }

    public void getFilm(final String filmId) {
        checkViewAttached();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.getTopFilmById(filmId)
                .subscribeOn(Schedulers.io())
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(film -> {
                    if (film == null) {
                        getMvpView().showUnknownError();
                    } else {
                        getMvpView().showFilmInfo(film);
                        //Init strings for sharing
                        initSharedInformation(film);
                    }
                }, throwable -> {
                    getMvpView().showUnknownError();
                    Timber.d("Error occurred!", throwable);
                    if (BuildConfig.DEBUG) {
                        throwable.printStackTrace();
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
                .subscribe(film -> {
                    if (film.favorite.equals(String.valueOf(ConstantsManager.NOT_FAVORITE))) {
                        mDataManager.addToFavorite(filmId);
                        getMvpView().showAddedToFavorites();
                    } else {
                        mDataManager.removeFromFavorite(filmId);
                        getMvpView().showRemovedFromFavorites();
                    }
                });
    }

    public String getImdbLink() {
       return mImdbLink;
    }

    private void initSharedInformation(FilmModel film) {
        mTitle = film.title;
        mRating = film.rating;
        mDirectors = film.directors;
        mGenres = film.genres;
        mImdbLink = film.urlIMDB;
    }
}
