package me.bitfrom.whattowatch.rest;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Constantine on 03.10.2015.
 */
public class LoggingInterceptor implements Interceptor {

    /**
     * Since Square removed setLogLevel() method in Retrofit 2.0, we have to implement
     * logging by ourselves
     * **/
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();

        Log.d("OkHttp", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.d("OkHttp", String.format("Received response from %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        Log.d("OkHttp", response.body().toString());

        return response;
    }
}
