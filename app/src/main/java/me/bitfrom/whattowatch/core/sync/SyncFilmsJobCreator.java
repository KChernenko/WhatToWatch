package me.bitfrom.whattowatch.core.sync;


import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import javax.inject.Inject;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class SyncFilmsJobCreator implements JobCreator {

    @Inject
    protected SyncFilmsJob syncFilmsJob;

    public SyncFilmsJobCreator() {
        WWApplication.getApplication().getComponent().inject(this);
    }

    @Override
    public Job create(String tag) {
        switch (tag) {
            case ConstantsManager.SYNC_FILMS_JOB_TAG:
                return syncFilmsJob;
            default:
                return null;
        }
    }

}