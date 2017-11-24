package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zwh.mvparms.eyepetizer.mvp.contract.AuthorDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.AttentionService;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.AuthorDetailService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;


@ActivityScope
public class AuthorDetailModel extends BaseModel implements AuthorDetailContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public AuthorDetailModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public Observable<VideoListInfo> getAuthorVideoList(int start) {
        Observable<VideoListInfo> observable = mRepositoryManager.obtainRetrofitService(AuthorDetailService.class)
                .getAuthorVideoList(start, 10);
        return observable;
    }

    @Override
    public Observable<AuthorTabsInfo> getAuthorTabs(int id) {
        Observable<AuthorTabsInfo> observable = mRepositoryManager.obtainRetrofitService(AuthorDetailService.class)
                .getAuthorTabs(id, "PGC");
        return observable;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}