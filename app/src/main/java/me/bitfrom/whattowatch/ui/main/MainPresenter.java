package me.bitfrom.whattowatch.ui.main;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.sync.SyncFilmsJob;
import me.bitfrom.whattowatch.ui.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private DataManager dataManager;
    private SyncFilmsJob syncFilmsJob;

    @Inject
    public MainPresenter(@NonNull DataManager dataManager,
                         @NonNull SyncFilmsJob syncFilmsJob) {
        this.dataManager = dataManager;
        this.syncFilmsJob = syncFilmsJob;
    }

    @Override
    public void attachView(@NonNull MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void initFirstSync() {
        if (dataManager.getPreferencesHelper().checkIfFirstLaunched()) {
            if (isViewAttached()) {
                getMvpView().navigateToIntroActivity();
                dataManager.getPreferencesHelper().markFirstLaunched();
            }
        } else {
            syncFilmsJob.scheduleSync();
        }
    }
}