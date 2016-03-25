package me.bitfrom.whattowatch.ui.base;

import android.support.annotation.NonNull;

public interface Presenter<V extends MvpView> {

    void attachView(@NonNull V mvpView);

    void detachView();
}
