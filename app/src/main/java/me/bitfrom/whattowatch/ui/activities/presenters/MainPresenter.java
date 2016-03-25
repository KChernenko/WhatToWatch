package me.bitfrom.whattowatch.ui.activities.presenters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.ui.activities.WWIntro;
import me.bitfrom.whattowatch.ui.activities.views.MainMvpView;
import me.bitfrom.whattowatch.ui.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private DataManager mDataManager;
    private Context mContext;

    @Inject
    public MainPresenter(@NonNull DataManager dataManager, @NonNull @ApplicationContext Context context) {
        mDataManager = dataManager;
        mContext = context;
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
        if (mDataManager.getPreferencesHelper().checkIfFirstLaunched()) {
            Intent intent = new Intent(mContext, WWIntro.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            mDataManager.getPreferencesHelper().markFirstLaunched();
        } else {
            FilmsSyncAdapter.initSyncAdapter(mContext);
        }
    }


}
