package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zwh.mvparms.eyepetizer.mvp.contract.AuthorDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.AuthorDetailService;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.VideoDetailService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import javax.inject.Inject;

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
    public Observable<VideoListInfo> getAuthorVideoList(int id, int start) {
        Observable<VideoListInfo> observable = mRepositoryManager.obtainRetrofitService(AuthorDetailService.class)
                .getAuthorVideoList(id,start, 10);
        return observable;
    }

    @Override
    public Observable<AuthorTabsInfo> getAuthorTabs(int id) {
        Observable<AuthorTabsInfo> observable = mRepositoryManager.obtainRetrofitService(AuthorDetailService.class)
                .getAuthorTabs(id, "PGC");
        return observable;
    }
    @Override
    public Observable<AuthorIndexInfo> getAuthorIndexInfo(int id) {
        Observable<AuthorIndexInfo> observable = mRepositoryManager.obtainRetrofitService(AuthorDetailService.class)
                .getAuthorIndexInfo(id, "PGC");
        return observable;
    }

    @Override
    public Observable<AuthorDynamicInfo> getAuthorDynamicList(int id, int startCount) {
        Observable<AuthorDynamicInfo> observable = mRepositoryManager.obtainRetrofitService(AuthorDetailService.class)
                .getAuthorDynamicList(id, startCount,10,"PGC");
        return observable;
    }

    @Override
    public Observable<AuthorAlbumInfo> getAuthorAlbumList(int id, int startCount) {
        Observable<AuthorAlbumInfo> observable = mRepositoryManager.obtainRetrofitService(AuthorDetailService.class)
                .getAuthorAlbumList(id,startCount,10);
        return observable;
    }

    @Override
    public Observable<ShareInfo> getShareInfo(int identity) {
        Observable<ShareInfo> observable = mRepositoryManager.obtainRetrofitService(VideoDetailService.class)
                .getShareInfo("PGC","WEB_PAGE",identity);
        return observable;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}