package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.jess.arms.utils.StringUtils;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.mvp.contract.HistoryContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDaoEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;


@ActivityScope
public class HistoryModel extends BaseModel implements HistoryContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HistoryModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<List<VideoDaoEntity>> getListFromDb(int start) {
        return Observable.create((ObservableOnSubscribe<List<VideoDaoEntity>>) e -> {
            VideoDaoEntity entity = new VideoDaoEntity();
            List<VideoDaoEntity> list = GreenDaoHelper.getInstance().create(entity.getDbName())
                    .getMaster().newSession()
                    .getVideoDaoEntityDao().queryBuilder()
                    .limit(10)
                    .offset(start)
                    .list();
            List<VideoDaoEntity> infolist = new ArrayList<VideoDaoEntity>();
            if (!StringUtils.isEmpty(list)){
                for (VideoDaoEntity entity1 : list){
                    entity1.setVideo(mGson.fromJson(entity1.getBody(),VideoListInfo.Video.VideoData.class));
                    infolist.add(entity1);
                }
            }
            e.onNext(infolist);
        });
    }
}