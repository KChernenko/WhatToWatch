package me.bitfrom.whattowatch.data.rest;

import me.bitfrom.whattowatch.data.model.FilmPojo;
import me.bitfrom.whattowatch.data.model.TheaterPojo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FilmsAPI {

    @GET("top?")
    Observable<FilmPojo> getTopFilms(@Query("start") String start,
                                 @Query("end") String end,
                                 @Query("token") String token,
                                 @Query("format") String format,
                                 @Query("data") String data);

    @GET("bottom?")
    Observable<FilmPojo> getBottomFilms(@Query("start") String start,
                                    @Query("end") String end,
                                    @Query("token") String token,
                                    @Query("format") String format,
                                    @Query("data") String data);

    @GET("inTheaters?")
    Observable<TheaterPojo> getInCinemas(@Query("token") String token,
                                               @Query("format") String format,
                                               @Query("language") String language);

    @GET("comingSoon?")
    Observable<TheaterPojo> getComingSoon(@Query("token") String token,
                                          @Query("format") String format);
}
