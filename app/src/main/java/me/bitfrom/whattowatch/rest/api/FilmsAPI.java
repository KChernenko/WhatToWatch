package me.bitfrom.whattowatch.rest.api;

import me.bitfrom.whattowatch.data.model.Film;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Constantine with love.
 */
public interface FilmsAPI {
    
    @GET("top?")
    Observable<Film> getFilms(@Query("start") String start,
                                    @Query("end") String end,
                                    @Query("token") String token,
                                    @Query("format") String format,
                                    @Query("data") String data);
}
