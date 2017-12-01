package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.jess.arms.widget.imageloader.ImageLoader;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoListActivityContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;


@ActivityScope
public class VideoListActivityPresenter extends BasePresenter<VideoListActivityContract.Model, VideoListActivityContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public VideoListActivityPresenter(VideoListActivityContract.Model model, VideoListActivityContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void getPlayListVideoList(int id,int start,boolean isLoadMore) {
        mModel.getPlayListVideoList(id,start).compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<VideoListInfo>(mErrorHandler) {
                    @Override
                    public void onNext(VideoListInfo info) {
                        mRootView.setVideos(info.getItemList(),isLoadMore);
                    }
                });
    }
    public void getPopularVideoList(int id,int start,boolean isLoadMore) {
        mModel.getPopularVideoList(id,"mostPopular",start).compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<VideoListInfo>(mErrorHandler) {
                    @Override
                    public void onNext(VideoListInfo info) {
                        mRootView.setVideos(info.getItemList(),isLoadMore);
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
