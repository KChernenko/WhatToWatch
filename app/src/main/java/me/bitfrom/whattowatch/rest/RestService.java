package me.bitfrom.whattowatch.rest;



import java.util.List;

import me.bitfrom.whattowatch.rest.model.Movie;
/**
 * Created by Constantine with love.
 */
public class RestService {

    private static final String REQUEST_FORMAT = "JSON";
    private static final String DATA_FORMAT = "F";
    private static final int START_POINT = 1;
    private static final int END_POINT = 250;
    private static final String TOKEN = "19c94797-333b-45b7-aded-4bdca4e857fa";


    public List<Movie> request() {
        RestClient restClient = new RestClient();

        return restClient.getMovieAPI().getMovies(REQUEST_FORMAT,
                START_POINT, END_POINT, DATA_FORMAT, TOKEN);
    }
}
