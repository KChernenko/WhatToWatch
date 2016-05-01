package me.bitfrom.whattowatch.core.rest;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.whattowatch.core.busevents.ServerErrorEvent;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RestModule {

    @Singleton
    @Provides
    public HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Singleton
    @Provides
    public StethoInterceptor providesStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Singleton
    @Provides
    public Interceptor providesResponseCodeInterceptor(@NonNull EventBus eventBus) {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            int responseCode = response.code();
            switch (responseCode) {
                case ConstantsManager.INTERNAL_SERVER_ERROR:
                case ConstantsManager.NOT_IMPLEMENTED:
                case ConstantsManager.BAD_GATEWAY:
                case ConstantsManager.SERVICE_UNAVAILABLE:
                case ConstantsManager.GATEWAY_TIME_OUT:
                case ConstantsManager.NETWORK_CONNECT_TIMEOUT_ERROR:
                    eventBus.post(new ServerErrorEvent());
                    break;
                default:
                    break;
            }

            return response;
        };
    }

    @Singleton
    @Provides
    public OkHttpClient providesHttpClient(@NonNull HttpLoggingInterceptor httpLoggingInterceptor,
                                           @NonNull StethoInterceptor stethoInterceptor,
                                           @NonNull Interceptor responseCodeInterceptor) {

        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(responseCodeInterceptor)
                .addNetworkInterceptor(stethoInterceptor)
                .connectTimeout(ConstantsManager.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConstantsManager.READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit(@NonNull OkHttpClient okHttpClient, @NonNull Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(ConstantsManager.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public FilmsAPI providesFilmsApi(@NonNull Retrofit retrofit) {
        return retrofit.create(FilmsAPI.class);
    }
}