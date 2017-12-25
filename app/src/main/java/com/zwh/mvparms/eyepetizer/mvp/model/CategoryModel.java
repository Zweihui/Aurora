package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zwh.mvparms.eyepetizer.mvp.contract.CategoryContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.cache.CommonCache;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.SplashService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.Reply;


@ActivityScope
public class CategoryModel extends BaseModel implements CategoryContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public CategoryModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}