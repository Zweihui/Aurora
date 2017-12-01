package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ReplyInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static android.R.attr.id;
import static android.R.attr.path;


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

    public void getRelaRelateVideoInfo(int id) {
        mModel.getRelateVideoInfo(id).compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<VideoListInfo>(mErrorHandler) {
                    @Override
                    public void onNext(VideoListInfo info) {
                        mRootView.setData(info, false);
                    }
                });
    }

    public void getSecondRelaRelateVideoInfo(String path, int id, int startnum) {
        mModel.getSecondRelateVideoInfo(path, id, startnum).compose(RxUtils.applySchedulersWithLifeCycle(mRootView))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        if (startnum==0){
                            mRootView.showLoading();
                        }
                    }
                })
                .subscribe(new ErrorHandleSubscriber<VideoListInfo>(mErrorHandler) {
                    @Override
                    public void onNext(VideoListInfo info) {
                        mRootView.setData(info, true);
                    }
                });
    }

    public void getReplyInfo(int videoId) {
        mModel.getAllReplyInfo(videoId).compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ReplyInfo>(mErrorHandler) {
                    @Override
                    public void onNext(ReplyInfo info) {
                        mRootView.setReplyData(info, false);
                    }
                });
    }
    public void getMoreReplyInfo(int lastId,int videoId) {
        mModel.getMoreReplyInfo(lastId,videoId).compose(RxUtils.applySchedulersWithLifeCycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<ReplyInfo>(mErrorHandler) {
                    @Override
                    public void onNext(ReplyInfo info) {
                        mRootView.setReplyData(info, true);
                    }
                });
    }
    public void getShareInfo(int videoId) {
        mModel.getShareInfo(videoId).compose(RxUtils.applySchedulersWithLifeCycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<ShareInfo>(mErrorHandler) {
                    @Override
                    public void onNext(ShareInfo info) {
                        mRootView.setShareData(info);
                    }
                });
    }
    public void getVideoData(int videoId) {
        mModel.getVideoData(videoId).compose(RxUtils.applySchedulersWithLifeCycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<VideoListInfo.Video.VideoData>(mErrorHandler) {
                    @Override
                    public void onNext(VideoListInfo.Video.VideoData data) {
                        mRootView.setVideoData(data);
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