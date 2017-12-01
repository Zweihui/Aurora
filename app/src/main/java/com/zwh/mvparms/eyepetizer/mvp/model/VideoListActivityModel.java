package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoListActivityContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.VideoService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;


@ActivityScope
public class VideoListActivityModel extends BaseModel implements VideoListActivityContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public VideoListActivityModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<VideoListInfo> getPopularVideoList(int id, String strategy,int start) {
        Observable<VideoListInfo> observable = mRepositoryManager.obtainRetrofitService(VideoService.class)
                .getPopularVideoList(id,strategy,start,10);
        return observable;
    }

    @Override
    public Observable<VideoListInfo> getPlayListVideoList(int id,int start) {
        Observable<VideoListInfo> observable = mRepositoryManager.obtainRetrofitService(VideoService.class)
                .getPlayListVideoList(id,start,10);
        return observable;
    }
}