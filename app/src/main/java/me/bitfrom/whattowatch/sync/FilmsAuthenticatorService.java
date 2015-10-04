package me.bitfrom.whattowatch.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Constantine with love.
 */
public class FilmsAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private FilmsAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        //Create a new authenticator object
        mAuthenticator = new FilmsAuthenticator(this);
    }

    /*
    * When the system binds to this Service to make the RPC call
    * return the authenticator's IBinder.
    */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
