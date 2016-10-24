package me.bitfrom.whattowatch.ui.settings;

import android.support.annotation.NonNull;

import com.evernote.android.job.JobManager;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.sync.SyncFilmsJob;
import me.bitfrom.whattowatch.ui.base.BasePresenter;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class SettingsPresenter extends BasePresenter<SettingsView> {

    private DataManager dataManager;
    private SyncFilmsJob syncFilmsJob;

    @Inject
    public SettingsPresenter(@NonNull DataManager dataManager,
                             @NonNull SyncFilmsJob syncFilmsJob) {
        this.dataManager = dataManager;
        this.syncFilmsJob = syncFilmsJob;
    }

    @Override
    public void attachView(@NonNull SettingsView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void rescheduleSync(@NonNull String interval) {
        dataManager.getPreferencesHelper().setUpdateInterval(interval);
        JobManager.instance().cancelAllForTag(ConstantsManager.SYNC_FILMS_JOB_TAG);
        syncFilmsJob.scheduleSync();
    }

    public void getNewValue(@NonNull String prefKey) {
        if (isViewAttached()) {
            getMvpView().loadNewValue(dataManager.getPreferencesHelper().getPreferenceByKey(prefKey));
        }
    }
}