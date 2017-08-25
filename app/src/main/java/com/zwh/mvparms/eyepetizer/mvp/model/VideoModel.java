package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zwh.mvparms.eyepetizer.mvp.contract.VideoContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.cache.CommonCache;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.UserService;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.VideoService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;

import static com.zwh.mvparms.eyepetizer.mvp.model.UserModel.USERS_PER_PAGE;


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
    public Observable<VideoListInfo> getVideoList(String type, int lastIdQueried, boolean update) {
        Observable<VideoListInfo> videoInfo = mRepositoryManager.obtainRetrofitService(VideoService.class)
                .getVideoList(type);
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