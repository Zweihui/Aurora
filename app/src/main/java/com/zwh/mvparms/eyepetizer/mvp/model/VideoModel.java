package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.cache.CommonCache;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.VideoService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.IndextVideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;


@ActivityScope
public class VideoModel extends BaseModel implements VideoContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public VideoModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<IndextVideoListInfo> getIndexVideoList(long date, int num) {
        Observable<IndextVideoListInfo> observable = mRepositoryManager.obtainRetrofitService(VideoService.class)
                .getIndexVideoList(date,num, Constants.UDID,Constants.VC,Constants.VN,Constants.DEVICEMODEL);
        return observable;
    }


    @Override
    public Observable<IndextVideoListInfo> getMoreIndexVideoList(int page) {
        Observable<IndextVideoListInfo> observable = mRepositoryManager.obtainRetrofitService(VideoService.class)
                .getMoreIndexVideoList(2,page,Constants.UDID,Constants.VC,Constants.VN,Constants.DEVICEMODEL);
        return observable;
    }

    @Override
    public Observable<VideoListInfo> getVideoList(String type, String lastIdQueried,int startCount,boolean update) {
        Observable<VideoListInfo> videoInfo = mRepositoryManager.obtainRetrofitService(VideoService.class)
                .getVideoList(startCount, Constants.HOME_VIDEO_LIST_PAGE_SIZE,type,Constants.UDID);
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return mRepositoryManager.obtainCacheService(CommonCache.class)
                .getVideoList(videoInfo
                        , new DynamicKey(lastIdQueried)
                        , new EvictDynamicKey(update))
                .flatMap(new Function<Reply<VideoListInfo>, ObservableSource<VideoListInfo>>() {
                    @Override
                    public ObservableSource<VideoListInfo> apply(@NonNull Reply<VideoListInfo> listReply) throws Exception {
                        return Observable.just(listReply.getData());
                    }
                });
    }

}