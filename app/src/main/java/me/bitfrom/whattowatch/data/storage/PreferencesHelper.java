package me.bitfrom.whattowatch.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.utils.ConstantsManager;

@Singleton
public class PreferencesHelper {

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(ConstantsManager.PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }
}
