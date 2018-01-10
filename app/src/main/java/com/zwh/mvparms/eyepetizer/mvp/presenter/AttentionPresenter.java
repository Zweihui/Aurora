package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.AttentionContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AttentionInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class AttentionPresenter extends BasePresenter<AttentionContract.Model, AttentionContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public AttentionPresenter(AttentionContract.Model model, AttentionContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void getAttentionVideoList(int start ,boolean isLoadMore) {
        mModel.getAttentionVideoList(start).compose(RxUtils.applySchedulers(mRootView,isLoadMore))
                .subscribe(new ErrorHandleSubscriber<AttentionInfo>(mErrorHandler) {
                    @Override
                    public void onNext(AttentionInfo info) {
                        mRootView.setData(info.getItemList(),isLoadMore);
                    }
                });
    }
    public void getMyAttentionList(String userid) {
        mModel.getMyAttentionList(userid).compose(RxUtils.applySchedulers(mRootView,false))
                .subscribe(new ErrorHandleSubscriber<List<MyAttentionEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<MyAttentionEntity> list) {
                        mRootView.setAuthorList(list);
                        onComplete();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}
