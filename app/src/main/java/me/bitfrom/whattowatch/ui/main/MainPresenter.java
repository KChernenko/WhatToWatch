package me.bitfrom.whattowatch.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.ui.intro.WWIntro;
import me.bitfrom.whattowatch.ui.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private Context context;
    private DataManager dataManager;

    @Inject
    public MainPresenter(@NonNull @ApplicationContext Context context,
                         @NonNull DataManager dataManager) {
        this.context = context;
        this.dataManager = dataManager;
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
        checkViewAttached();
        if (dataManager.getPreferencesHelper().checkIfFirstLaunched()) {
            Intent intent = new Intent(context, WWIntro.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            dataManager.getPreferencesHelper().markFirstLaunched();
        } else {
            FilmsSyncAdapter.initSyncAdapter(context);
        }
    }
}