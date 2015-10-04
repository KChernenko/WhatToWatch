package me.bitfrom.whattowatch.rest;

import com.squareup.okhttp.OkHttpClient;

import me.bitfrom.whattowatch.rest.api.MovieAPI;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Constantine with love.
 */
public class RestClient {

    private static final String BASE_URL = "http://www.myapifilms.com/imdb/";
    private MovieAPI movieAPI;

    public RestClient() {

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new LoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        movieAPI = retrofit.create(MovieAPI.class);
    }

    public MovieAPI getMovieAPI() {
        return movieAPI;
    }
}
