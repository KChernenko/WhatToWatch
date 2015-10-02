package me.bitfrom.whattowatch.rest;

import me.bitfrom.whattowatch.rest.api.MovieAPI;
/**
 * Created by Constantine with love.
 */
public class RestClient {

    private static final String BASE_URL = "http://www.myapifilms.com/imdb";
    private MovieAPI movieAPI;

    public RestClient() {

//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .setEndpoint(BASE_URL)
//                .build();
//
//        movieAPI = restAdapter.create(MovieAPI.class);
    }

    public MovieAPI getMovieAPI() {
        return movieAPI;
    }
}
