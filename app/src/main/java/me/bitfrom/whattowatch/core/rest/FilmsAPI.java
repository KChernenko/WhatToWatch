package me.bitfrom.whattowatch.core.rest;

import android.support.annotation.NonNull;

import me.bitfrom.whattowatch.core.model.FilmPojo;
import me.bitfrom.whattowatch.core.model.TheaterPojo;
import me.bitfrom.whattowatch.core.model.comingsoon.NewMoviesPojo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FilmsAPI {

    @GET("top?") @NonNull
    Observable<FilmPojo> getTopFilms(@Query("start") String start,
                                     @Query("end") String end,
                                     @Query("token") String token,
                                     @Query("format") String format,
                                     @Query("data") String data);

    @GET("bottom?") @NonNull
    Observable<FilmPojo> getBottomFilms(@Query("start") String start,
                                        @Query("end") String end,
                                        @Query("token") String token,
                                        @Query("format") String format,
                                        @Query("data") String data);

    @GET("inTheaters?") @NonNull
    Observable<TheaterPojo> getInCinemas(@Query("token") String token,
                                         @Query("format") String format,
                                         @Query("language") String language);

    @GET("comingSoon?") @NonNull
    Observable<NewMoviesPojo> getComingSoon(@Query("token") String token,
                                            @Query("format") String format);
}
