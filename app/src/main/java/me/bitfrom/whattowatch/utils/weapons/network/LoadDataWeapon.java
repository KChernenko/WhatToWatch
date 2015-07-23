package me.bitfrom.whattowatch.utils.weapons.network;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.bitfrom.whattowatch.rest.RestService;
import me.bitfrom.whattowatch.rest.model.Movie;
import me.bitfrom.whattowatch.utils.MovieGenerator;
import me.bitfrom.whattowatch.utils.Utility;

/**
 * Created by Constantine with love.
 */
public class LoadDataWeapon {

    private static HashSet<Integer> randomMoviesNumbers;
    private static List<Movie> moviesContainer;
    private static List<Movie> movieList = new ArrayList<>();

    public static List<Movie> loadMovies(Context context) {
        int numberOfMovies = Utility.getPreferredNumbersOfMovies(context);

        RestService restService = new RestService();
        moviesContainer = restService.request();

        randomMoviesNumbers = new HashSet<>();

        while (randomMoviesNumbers.size() < numberOfMovies) {
            randomMoviesNumbers.add(MovieGenerator.getGenerator().getRandomMovieID());
        }

        for (Integer randomItem: randomMoviesNumbers) {
            movieList.add(moviesContainer.get(randomItem));
        }

        return movieList;
    }
}