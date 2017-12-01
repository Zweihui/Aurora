package com.zwh.mvparms.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.jess.arms.utils.StringUtils;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.contract.AttentionContract;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.AttentionService;
import com.zwh.mvparms.eyepetizer.mvp.model.api.service.VideoService;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AttentionInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.IndextVideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDaoEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;


@ActivityScope
public class AttentionModel extends BaseModel implements AttentionContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public AttentionModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }


    @Override
    public Observable<AttentionInfo> getAttentionVideoList(int start) {
        Observable<AttentionInfo> observable = mRepositoryManager.obtainRetrofitService(AttentionService.class)
                .getAttentionVideoList(start, 2);
        return observable;
    }
    @Override
    public Observable<List<MyAttentionEntity>> getMyAttentionList(String userid) {
        return Observable.create((ObservableOnSubscribe<List<MyAttentionEntity>>) emitter -> {

            BmobQuery<MyAttentionEntity> query = new BmobQuery<MyAttentionEntity>();
            query.addWhereEqualTo("userId", userid);
            query.order("-createdAt");
            query.findObjects(new FindListener<MyAttentionEntity>() {
                @Override
                public void done(List<MyAttentionEntity> list, BmobException e) {
                    emitter.onNext(list);
                }
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}