package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.contract.SearchContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.cache.CommonCache;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.SearchService;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.VideoService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;

import static android.R.attr.type;


@ActivityScope
public class SearchModel extends BaseModel implements SearchContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public SearchModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<List<String>> getHotWord() {
        Observable<List<String>> strs = mRepositoryManager.obtainRetrofitService(SearchService.class)
                .getHotWord();
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return mRepositoryManager.obtainCacheService(CommonCache.class)
                .getHotWords(strs)
                .flatMap(new Function<Reply<List<String>>, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(@NonNull Reply<List<String>> listReply) throws Exception {
                        return Observable.just(listReply.getData());
                    }
                });
    }

    @Override
    public Observable<VideoListInfo> getSearchList(String start,String query) {
        Observable<VideoListInfo> observable = mRepositoryManager.obtainRetrofitService(SearchService.class)
                .getSearchList(start,query);
        return observable;
    }
}