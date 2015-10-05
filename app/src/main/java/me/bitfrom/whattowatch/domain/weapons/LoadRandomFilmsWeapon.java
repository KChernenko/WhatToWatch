package me.bitfrom.whattowatch.domain.weapons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.rest.RestClient;
import me.bitfrom.whattowatch.rest.model.Film;
import me.bitfrom.whattowatch.utils.FilmIdGenerator;
import me.bitfrom.whattowatch.utils.Utility;
import me.bitfrom.whattowatch.utils.bus.RestErrorEvent;
import me.bitfrom.whattowatch.utils.bus.RestSuccessEvent;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Constantine with love.
 */
public class LoadRandomFilmsWeapon {

    public static void loadFilms() {
        RestClient restClient = new RestClient();

        restClient.getFilmsAPI().getMovies(WWApplication.getToken())
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<Film>, List<Film>>() {
                    @Override
                    public List<Film> call(List<Film> movies) {
                        List<Film> finalList = new ArrayList<>();

                        for (Integer randomItem: getRandomFilmsIds()) {
                            finalList.add(movies.get(randomItem));
                        }

                        return finalList;
                    }
                })
                .cache()
                .subscribe(new Observer<List<Film>>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new RestSuccessEvent("List of films was updated!"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new RestErrorEvent("Something went wrong! Try later."));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Film> movies) {
                        SaveDataWeapon.saveData(movies);
                    }
                });

    }

    /**
     * Returns HashSet of random generated ids
     * @return HashSet of generated ids
     * **/
    private static HashSet<Integer> getRandomFilmsIds() {
        int numberOfMovies =
                Utility.getPreferredNumbersOfMovies(WWApplication.getAppContext());

        HashSet<Integer> randomMoviesNumbers = new HashSet<>();

        while (randomMoviesNumbers.size() < numberOfMovies) {
            randomMoviesNumbers.add(FilmIdGenerator.getGenerator().getRandomFilmID());
        }

        return randomMoviesNumbers;
    }
}