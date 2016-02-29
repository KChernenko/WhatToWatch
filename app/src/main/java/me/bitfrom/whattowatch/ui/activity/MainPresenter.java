package me.bitfrom.whattowatch.ui.activity;

import android.content.Context;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.DataManager;
import me.bitfrom.whattowatch.core.sync.FilmsSyncAdapter;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.ui.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private DataManager mDataManager;
    private Context mContext;

    @Inject
    public MainPresenter(DataManager dataManager, @ApplicationContext Context context) {
        mDataManager = dataManager;
        mContext = context;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void initFirstSync() {
        checkViewAttached();
        if (mDataManager.getPreferencesHelper().checkIfFirstLaunched()) {
            FilmsSyncAdapter.initSyncAdapter(mContext);
            mDataManager.getPreferencesHelper().markFirstLaunched();
            getMvpView().showDataStartSyncing();
        }
    }
}
