package me.bitfrom.whattowatch.rest;

import android.util.Log;

import java.util.List;

import me.bitfrom.whattowatch.rest.model.Movie;
import me.bitfrom.whattowatch.utils.MovieGenerator;

/**
 * Created by Constantine with love.
 */
public class RestService {

    private static final String LOG_TAG = RestService.class.getSimpleName();
    private static final String REQUEST_FORMAT = "JSON";
    private static final String DATA_FORMAT = "F";
    private static final int START_POINT = 1;
    private static final int END_POINT = 250;


    public Movie request() {
        List<Movie> requestResult;
        int randomMovieNumber = MovieGenerator.getGenerator().getRandomMovieID();
        Log.d(LOG_TAG, "" + randomMovieNumber);
        RestClient restClient = new RestClient();
        requestResult = restClient.getMovieAPI().getMovie(REQUEST_FORMAT,
                START_POINT, END_POINT, DATA_FORMAT);

        return requestResult.get(randomMovieNumber);
    }
}
