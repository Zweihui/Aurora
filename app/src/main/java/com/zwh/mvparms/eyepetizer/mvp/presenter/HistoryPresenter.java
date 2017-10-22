package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.HistoryContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDaoEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class HistoryPresenter extends BasePresenter<HistoryContract.Model, HistoryContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public HistoryPresenter(HistoryContract.Model model, HistoryContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }


    public void getListFromDb(int start, boolean isLoadMore) {
        mModel.getListFromDb(start)
                .delay(600, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<VideoDaoEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull List<VideoDaoEntity> videos) {
                        mRootView.setData(videos,isLoadMore);
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        mRootView.hideLoading();
                    }
                });

    }
    public void deleteFromDb(VideoDaoEntity daoEntity,final int position) {
        mModel.deleteFromDb(daoEntity)
                .delay(600, TimeUnit.MILLISECONDS)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<Boolean>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull Boolean succeed) {
                        mRootView.deleteData(position);
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        mRootView.hideLoading();
                    }
                });

    }

    public Observable<List<VideoListInfo.Video>> getListFromNet() {
        return null;
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
