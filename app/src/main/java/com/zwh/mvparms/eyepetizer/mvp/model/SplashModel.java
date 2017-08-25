package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zwh.mvparms.eyepetizer.mvp.contract.SplashContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.cache.CommonCache;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.SplashService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;


@ActivityScope
public class SplashModel extends BaseModel implements SplashContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public SplashModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<List<Category>> getCategories() {
        Observable<List<Category>> categories = mRepositoryManager.obtainRetrofitService(SplashService.class)
                .getCategories();
        return mRepositoryManager.obtainCacheService(CommonCache.class)
                .getCategories(categories)
                .flatMap(new Function<Reply<List<Category>>, ObservableSource<List<Category>>>() {
                    @Override
                    public ObservableSource<List<Category>> apply(@NonNull Reply<List<Category>> listReply) throws Exception {
                        return Observable.just(listReply.getData());
                    }
                });
    }
}