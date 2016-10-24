package me.bitfrom.whattowatch.ui.settings;

import android.support.annotation.NonNull;

import me.bitfrom.whattowatch.ui.base.MvpView;

public interface SettingsView extends MvpView {

    void loadNewValue(@NonNull String newValue);

}
