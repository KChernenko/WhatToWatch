package me.bitfrom.whattowatch.ui.activities.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.injection.ActivityContext;
import me.bitfrom.whattowatch.ui.activities.views.DetailMvpView;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private Context context;
    private final DataManager dataManager;

    private Subscription subscription;

    private String title;
    private String rating;
    private String directors;
    private String genres;
    private String imdbLink;

    @Inject
    public DetailPresenter(@NonNull @ActivityContext Context context,
                           @NonNull DataManager dataManager) {
        this.context = context;
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(@NonNull DetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    public void getFilm(@NonNull final String filmId) {
        checkViewAttached();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = dataManager.getFilmById(filmId)
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
                    Timber.e(throwable, "Error occurred while retrieving film's info!");
                });
    }

    public void shareWithFriends() {
        checkViewAttached();
        String sharedInfo = " «" +
                title + "»" + "\n" +
                context.getString(R.string.share_action_imdb_rating) + " " + rating +
                ".\n" + context.getString(R.string.share_action_directors) + " " +
                directors + "\n" + genres + "\n" +
                context.getString(R.string.app_hash_tag);

        getMvpView().shareWithFriends(sharedInfo);
    }

    public void updateFavorites(@NonNull final String filmId) {
        checkViewAttached();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = dataManager.getFilmById(filmId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(film -> {
                    if (film.inFavorite() == ConstantsManager.NOT_FAVORITE) {
                        dataManager.addToFavorite(filmId);
                        getMvpView().showAddedToFavorites();
                    } else {
                        dataManager.removeFromFavorite(filmId);
                        getMvpView().showRemovedFromFavorites();
                    }
                });
    }

    public String getImdbLink() {
        return imdbLink;
    }

    private void initSharedInformation(@NonNull FilmEntity film) {
        title = film.title();
        rating = film.rating();
        directors = film.directors();
        genres = film.genres();
        imdbLink = film.imbdUrl();
    }
}