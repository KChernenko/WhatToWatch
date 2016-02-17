package me.bitfrom.whattowatch.rest;


import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.rest.api.FilmsAPI;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Constantine with love.
 */
public class RestClient {

    private static final String BASE_URL = "http://www.myapifilms.com/imdb/";
    private FilmsAPI filmsAPI;

    public RestClient() {

        OkHttpClient client = new OkHttpClient();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        filmsAPI = retrofit.create(FilmsAPI.class);
    }

    public FilmsAPI getFilmsAPI() {
        return filmsAPI;
    }
}
