package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zwh.mvparms.eyepetizer.mvp.contract.VideoDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.VideoDetailService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ReplyInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;

import static android.R.attr.id;
import static android.R.attr.path;


@ActivityScope
public class VideoDetailModel extends BaseModel implements VideoDetailContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public VideoDetailModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<VideoListInfo> getRelateVideoInfo(int id) {
         Observable<VideoListInfo> observable = mRepositoryManager.obtainRetrofitService(VideoDetailService.class)
                .getRelateVideoInfo(id);
        return observable;
    }

    @Override
    public Observable<VideoListInfo> getSecondRelateVideoInfo(String path, int id,int startCount) {
        Observable<VideoListInfo> observable = mRepositoryManager.obtainRetrofitService(VideoDetailService.class)
                .getSecondRelateVideoInfo(path,id,startCount,10);
        return observable;
    }

    @Override
    public Observable<ReplyInfo> getAllReplyInfo(int videoId) {
        Observable<ReplyInfo> observable = mRepositoryManager.obtainRetrofitService(VideoDetailService.class)
                .getAllReplyInfo(videoId);
        return observable;
    }

    @Override
    public Observable<ReplyInfo> getMoreReplyInfo(int lastId, int videoId) {
        Observable<ReplyInfo> observable = mRepositoryManager.obtainRetrofitService(VideoDetailService.class)
                .getMoreReplyInfo(lastId, videoId, "video");
        return observable;
    }

    @Override
    public Observable<ShareInfo> getShareInfo(int identity) {
        Observable<ShareInfo> observable = mRepositoryManager.obtainRetrofitService(VideoDetailService.class)
                .getShareInfo("VIDEO","WEB_PAGE",identity);
        return observable;
    }

    @Override
    public Observable<VideoListInfo.Video.VideoData> getVideoData(int id) {
        Observable<VideoListInfo.Video.VideoData> observable = mRepositoryManager.obtainRetrofitService(VideoDetailService.class)
                .getVideoData(id);
        return observable;
    }
}