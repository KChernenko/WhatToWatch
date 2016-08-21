package me.bitfrom.whattowatch.core.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FilmsAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private FilmsAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        //Create a new authenticator object
        authenticator = new FilmsAuthenticator(this);
    }

    /*
    * When the system binds to this Service to make the RPC call
    * return the authenticator's IBinder.
    */
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}