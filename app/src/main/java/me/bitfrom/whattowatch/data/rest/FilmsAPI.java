package me.bitfrom.whattowatch.data.rest;

import me.bitfrom.whattowatch.data.model.Film;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FilmsAPI {

    @GET("top?")
    Observable<Film> getTopFilms(@Query("start") String start,
                                 @Query("end") String end,
                                 @Query("token") String token,
                                 @Query("format") String format,
                                 @Query("data") String data);

    @GET("bottom?")
    Observable<Film> getBottomFilms(@Query("start") String start,
                                    @Query("end") String end,
                                    @Query("token") String token,
                                    @Query("format") String format,
                                    @Query("data") String data);
}
