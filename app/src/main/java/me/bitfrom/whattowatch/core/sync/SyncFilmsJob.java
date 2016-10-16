package me.bitfrom.whattowatch.core.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.NotificationHelper;
import me.bitfrom.whattowatch.core.storage.PreferencesHelper;
import me.bitfrom.whattowatch.core.sync.services.SyncBottomFilmsService;
import me.bitfrom.whattowatch.core.sync.services.SyncComingSoonService;
import me.bitfrom.whattowatch.core.sync.services.SyncInCinemasFilmsService;
import me.bitfrom.whattowatch.core.sync.services.SyncTopFilmsService;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class SyncFilmsJob extends Job {

    private PreferencesHelper preferencesHelper;
    private NotificationHelper notificationHelper;

    @Inject
    public SyncFilmsJob(@NonNull PreferencesHelper preferencesHelper,
                        @NonNull NotificationHelper notificationHelper) {
        this.preferencesHelper = preferencesHelper;
        this.notificationHelper = notificationHelper;
    }

    @NonNull @Override
    protected Result onRunJob(Params params) {
        Context context = getContext();

        context.startService(new Intent(context, SyncTopFilmsService.class));
        context.startService(new Intent(context, SyncBottomFilmsService.class));
        context.startService(new Intent(context, SyncInCinemasFilmsService.class));
        context.startService(new Intent(context, SyncComingSoonService.class));

        notificationHelper.showNotification();

        return Result.SUCCESS;
    }

    public void scheduleSync() {
        if (JobManager.instance()
                .getAllJobRequestsForTag(ConstantsManager.SYNC_FILMS_JOB_TAG).size() < 1) {
            long interval = preferencesHelper.getUpdateInterval();
            long flex = interval / 10;
            new JobRequest.Builder(ConstantsManager.SYNC_FILMS_JOB_TAG)
                    .setPeriodic(interval, flex)
                    .setPersisted(true)
                    .setUpdateCurrent(true)
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(JobRequest.NetworkType.NOT_ROAMING)
                    .build()
                    .schedule();
        }
    }

}