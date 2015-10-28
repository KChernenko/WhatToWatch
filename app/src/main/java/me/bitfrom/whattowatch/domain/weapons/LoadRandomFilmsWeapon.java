package me.bitfrom.whattowatch.domain.weapons;

import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.rest.RestClient;
import me.bitfrom.whattowatch.rest.model.Film;
import me.bitfrom.whattowatch.utils.Utility;
import me.bitfrom.whattowatch.utils.bus.RestErrorEvent;
import me.bitfrom.whattowatch.utils.bus.RestSuccessfulEvent;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
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

        subscription = restClient.getFilmsAPI().getFilms(WWApplication.getToken())
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<Film>, List<Film>>() {
                    @Override
                    public List<Film> call(List<Film> films) {
                        Collections.shuffle(films);

                        return films.subList(0, Utility.getPreferredNumbersOfMovies(WWApplication.getAppContext()));
                    }
                })
                .cache()
                .subscribe(new Observer<List<Film>>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new RestSuccessfulEvent(SUCCESS_MESSAGE));
                        subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new RestErrorEvent(FAILURE_MESSAGE));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Film> films) {
                        SaveDataWeapon.saveData(films);
                    }
                });

    }
}