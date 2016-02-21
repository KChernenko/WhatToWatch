package me.bitfrom.whattowatch.ui.activity;


import javax.inject.Inject;

import me.bitfrom.whattowatch.ui.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainMvpView> {

    @Inject
    public MainPresenter() {}

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
