package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.contract.HotContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.cache.CommonCache;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.VideoService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.IndextVideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;


@ActivityScope
public class HotModel extends BaseModel implements HotContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HotModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<VideoListInfo> getRankVideoList(String Stra) {
        Observable<VideoListInfo> observable = mRepositoryManager.obtainRetrofitService(VideoService.class)
                .getRankVideoList(Stra);
        return mRepositoryManager.obtainCacheService(CommonCache.class)
                .getRankVideoList(observable
                        , new DynamicKey(Stra)
                        , new EvictDynamicKey(true))
                .flatMap(new Function<Reply<VideoListInfo>, ObservableSource<VideoListInfo>>() {
                    @Override
                    public ObservableSource<VideoListInfo> apply(@NonNull Reply<VideoListInfo> listReply) throws Exception {
                        return Observable.just(listReply.getData());
                    }
                });
    }
}