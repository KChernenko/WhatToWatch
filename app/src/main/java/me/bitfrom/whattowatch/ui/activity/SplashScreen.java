package me.bitfrom.whattowatch.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.ui.base.BaseActivity;

/**
 * Created by Constantine with love.
 */
public class SplashScreen extends BaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
