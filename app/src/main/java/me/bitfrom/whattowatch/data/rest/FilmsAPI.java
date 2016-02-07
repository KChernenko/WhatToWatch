package me.bitfrom.whattowatch.data.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.data.model.Film;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface FilmsAPI {

    @GET("top?")
    Observable<Film> getFilms(@Query("start") String start,
                              @Query("end") String end,
                              @Query("token") String token,
                              @Query("format") String format,
                              @Query("data") String data);

    /***
     * Helper class that sets up a new services
     ***/
    class Create {

        public static FilmsAPI filmsAPI() {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

            OkHttpClient client = new OkHttpClient();
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                client.interceptors().add(interceptor);
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsManager.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit.create(FilmsAPI.class);
        }
    }
}
