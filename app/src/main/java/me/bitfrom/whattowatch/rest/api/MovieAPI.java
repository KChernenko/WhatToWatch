package me.bitfrom.whattowatch.rest.api;

import java.util.List;

import me.bitfrom.whattowatch.rest.model.Movie;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Constantine with love.
 */
public interface MovieAPI {
    
    @GET("/top")
    public List<Movie> getMovie(@Query("format") String format, @Query("start") int start, @Query("end") int end,
                                @Query("data") String data);
}
