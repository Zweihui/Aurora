package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.AuthorDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class AuthorDetailPresenter extends BasePresenter<AuthorDetailContract.Model, AuthorDetailContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public AuthorDetailPresenter(AuthorDetailContract.Model model, AuthorDetailContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void getAuthorVideoList(int id,int start ,boolean isLoadMore) {
        mModel.getAuthorVideoList(id,start).compose(RxUtils.applySchedulers(mRootView,isLoadMore))
                .subscribe(new ErrorHandleSubscriber<VideoListInfo>(mErrorHandler) {
                    @Override
                    public void onNext(VideoListInfo info) {
                        mRootView.setVideosData(info.getItemList(),isLoadMore);
                    }
                });
    }
    public void getAuthorTabs(int id) {
        mModel.getAuthorTabs(id).compose(RxUtils.applySchedulersWithLifeCycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<AuthorTabsInfo>(mErrorHandler) {
                    @Override
                    public void onNext(AuthorTabsInfo info) {
                        mRootView.setTabs(info);
                    }
                });
    }
    public void getShareInfo(int id) {
        mModel.getShareInfo(id).compose(RxUtils.applySchedulersWithLifeCycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<ShareInfo>(mErrorHandler) {
                    @Override
                    public void onNext(ShareInfo info) {
                        mRootView.setShareInfo(info);
                    }
                });
    }
    public void getAuthorIndexInfo(int id) {
        mModel.getAuthorIndexInfo(id).compose(RxUtils.applySchedulersWithLifeCycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<AuthorIndexInfo>(mErrorHandler) {
                    @Override
                    public void onNext(AuthorIndexInfo info) {
                        mRootView.setIndexInfo(info);
                    }
                });
    }
    public void getAuthorDynamicList(int id, int startCount, boolean isLoadMore) {
        mModel.getAuthorDynamicList(id,startCount).compose(RxUtils.applySchedulers(mRootView,isLoadMore))
                .subscribe(new ErrorHandleSubscriber<AuthorDynamicInfo>(mErrorHandler) {
                    @Override
                    public void onNext(AuthorDynamicInfo info) {
                        mRootView.setAuthorDynamicInfo(info.getItemList(),isLoadMore);
                    }
                });
    }
    public void getAuthorAlbumList(int id, int startCount, boolean isLoadMore) {
        mModel.getAuthorAlbumList(id,startCount).compose(RxUtils.applySchedulers(mRootView,isLoadMore))
                .subscribe(new ErrorHandleSubscriber<AuthorAlbumInfo>(mErrorHandler) {
                    @Override
                    public void onNext(AuthorAlbumInfo info) {
                        mRootView.setAuthorAlbumInfo(info.getItemList(),isLoadMore);
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
