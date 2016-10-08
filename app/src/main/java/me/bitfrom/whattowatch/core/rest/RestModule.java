package me.bitfrom.whattowatch.core.rest;

import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.core.busevents.ServerErrorEvent;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import timber.log.Timber;

@Module
public class RestModule {

    @Provides @Singleton
    public HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides @Singleton
    public StethoInterceptor providesStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides @Singleton
    public Interceptor providesResponseCodeAndCacheInterceptor(@NonNull EventBus eventBus) {
        return chain -> {
            Response response = chain.proceed(chain.request());

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

            // re-write response header to force use of cache
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(2, TimeUnit.DAYS)//Because content isn't time sensitive
                    .build();

            return response.newBuilder()
                    .header(ConstantsManager.CACHE_CONTROL_HEADER, cacheControl.toString())
                    .build();
        };
    }

    @Provides @Singleton
    public Cache providesCache(@NonNull Application application) {
        Cache cache = null;

        try {
            cache = new Cache(new File(application.getCacheDir(), ConstantsManager.CACHE_DIR_NAME),
                    ConstantsManager.CACHE_SIZE);
        } catch (Exception ex) {
            Timber.e(ex, "Failed to create cache directory!");
        }

        return cache;
    }

    @Provides @Singleton
    public OkHttpClient providesHttpClient(@NonNull HttpLoggingInterceptor httpLoggingInterceptor,
                                           @NonNull StethoInterceptor stethoInterceptor,
                                           @NonNull Interceptor responseCodeInterceptor,
                                           @NonNull Cache cache) {

        OkHttpClient.Builder okHttp = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            okHttp.addInterceptor(httpLoggingInterceptor);
            okHttp.addNetworkInterceptor(stethoInterceptor);
        }
        okHttp.addInterceptor(responseCodeInterceptor);
        okHttp.cache(cache);
        okHttp.connectTimeout(ConstantsManager.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okHttp.readTimeout(ConstantsManager.READ_TIMEOUT, TimeUnit.SECONDS);

        return okHttp.build();
    }

    @Singleton @Provides
    public Retrofit providesRetrofit(@NonNull OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ConstantsManager.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides @Singleton
    public FilmsAPI providesFilmsApi(@NonNull Retrofit retrofit) {
        return retrofit.create(FilmsAPI.class);
    }
}