package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.SearchContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.IndextVideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class SearchPresenter extends BasePresenter<SearchContract.Model, SearchContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public SearchPresenter(SearchContract.Model model, SearchContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void getHotWords() {
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {

            }

            @Override
            public void onRequestPermissionFailure() {
                mRootView.showMessage("Request permissons failure");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
        mModel.getHotWord().compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<String>>(mErrorHandler) {
                    @Override
                    public void onNext(List<String> list) {
                        mRootView.setHotWordData(list);
                    }
                });
    }
    public void getSearchList(String start,String query,boolean isLoadMore) {
        mModel.getSearchList(start,query).compose(RxUtils.applySchedulers(mRootView))
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (!isLoadMore){
                            mRootView.hideLoading();
                        }
                    }
                })
                .subscribe(new ErrorHandleSubscriber<VideoListInfo>(mErrorHandler) {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (!isLoadMore){
                            mRootView.hideLoading();
                        }
                    }

                    @Override
                    public void onNext(VideoListInfo info) {
                        mRootView.setListData(filter(info),isLoadMore,info.getTotal());
                    }
                });
    }

    private List<VideoListInfo.Video> filter(VideoListInfo info) {
        List<VideoListInfo.Video> list = new ArrayList<>();
        for (VideoListInfo.Video itemList: info.getItemList()){
            if (itemList.getType().equals("video")){
                list.add(itemList);
            }
        }
        return list;
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
