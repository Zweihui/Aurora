package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.RelateVideoInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class VideoDetailPresenter extends BasePresenter<VideoDetailContract.Model, VideoDetailContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public VideoDetailPresenter(VideoDetailContract.Model model, VideoDetailContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void getRelaRelateVideoInfo(int id){
        mModel.getRelateVideoInfo(id).compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<RelateVideoInfo>(mErrorHandler) {
                    @Override
                    public void onNext(RelateVideoInfo info) {
                        mRootView.setData(info);
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