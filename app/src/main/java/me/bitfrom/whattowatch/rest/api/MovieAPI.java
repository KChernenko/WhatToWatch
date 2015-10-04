package me.bitfrom.whattowatch.rest.api;

import java.util.List;

import me.bitfrom.whattowatch.rest.model.Film;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Constantine with love.
 */
public interface MovieAPI {
    
    @GET("top?format=JSON&start=1&end=250&data=F")
    Observable<List<Film>> getMovies(@Query("token") String token);
}
