package me.bitfrom.whattowatch.core.rest;

import android.support.annotation.NonNull;

import me.bitfrom.whattowatch.core.model.FilmModel;
import me.bitfrom.whattowatch.core.model.TheaterModel;
import me.bitfrom.whattowatch.core.model.comingsoon.NewMoviesModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FilmsAPI {

    @GET("top?") @NonNull
    Observable<FilmModel> getTopFilms(@Query("start") String start,
                                      @Query("end") String end,
                                      @Query("token") String token,
                                      @Query("format") String format,
                                      @Query("data") String data);

    @GET("bottom?") @NonNull
    Observable<FilmModel> getBottomFilms(@Query("start") String start,
                                         @Query("end") String end,
                                         @Query("token") String token,
                                         @Query("format") String format,
                                         @Query("data") String data);

    @GET("inTheaters?") @NonNull
    Observable<TheaterModel> getInCinemas(@Query("token") String token,
                                          @Query("format") String format,
                                          @Query("language") String language);

    @GET("comingSoon?") @NonNull
    Observable<NewMoviesModel> getComingSoon(@Query("token") String token,
                                             @Query("format") String format);
}