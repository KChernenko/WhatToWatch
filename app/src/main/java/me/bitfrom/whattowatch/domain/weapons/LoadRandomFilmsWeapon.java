package me.bitfrom.whattowatch.domain.weapons;

import java.util.Collections;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.rest.RestClient;
import me.bitfrom.whattowatch.data.model.Film;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import me.bitfrom.whattowatch.utils.Utility;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by Constantine with love.
 */
public class LoadRandomFilmsWeapon {

    /** Event messages **/
    private static final String SUCCESS_MESSAGE = "List of films was updated!";
    private static final String FAILURE_MESSAGE = "Something went wrong! Try later.";

    /** Less objects - less memory **/
    private static RestClient restClient = new RestClient();

    /**
     * We don't want that the Observer holds a strong reference to the Observable.
     * So, we have to unsubscribe, if we want to prevent memory leak.
     * **/
    private static Subscription subscription;

    /**
     * The method where our "magic" happens. Loads data from the server using RxJava,
     * shuffle collection of the received data, takes selected amount of items form the
     * SharedPreferences and passes the list of items to the save method. Need to be synchronized,
     * because it possible that user wants to update the data when SyncAdapter prepared to update it.
     * **/
    public static synchronized void loadFilms() {

        subscription = restClient.getFilmsAPI().getFilms(ConstantsManager.API_LIST_START,
                ConstantsManager.API_LIST_END, ConstantsManager.API_TOKEN, ConstantsManager.API_FORMAT,
                ConstantsManager.API_DATA)
                .subscribeOn(Schedulers.io())
                .cache()
                .subscribe(new Observer<Film>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Film film) {
                        Collections.shuffle(film.getData().getMovies());

                        SaveDataWeapon.saveData(film.getData().getMovies()
                                .subList(0, Utility.getPreferredNumbersOfMovies(WWApplication.getAppContext())));
                    }
                });

    }
}