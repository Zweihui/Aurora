package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.IndextVideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.UserAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.type;
import static android.R.id.list;


@ActivityScope
public class VideoPresenter extends BasePresenter<VideoContract.Model, VideoContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private boolean isFirst = true;
    private int lastId = 1;

    @Inject
    public VideoPresenter(VideoContract.Model model, VideoContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void getVideoList(String type,int queryId,int startCount,final boolean pullToRefresh) {
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {

            }

            @Override
            public void onRequestPermissionFailure() {
                mRootView.showMessage("Request permissons failure");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
        if (pullToRefresh) lastId = 1;//下拉刷新默认只请求第一页
        else lastId = queryId;
        boolean cache = pullToRefresh;
        if (pullToRefresh&&isFirst){
            cache =false;
            isFirst = false;
        }
        mModel.getVideoList(type,type+lastId,startCount,cache).compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<VideoListInfo>(mErrorHandler) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (pullToRefresh)
                        mRootView.showLoading();
                    }

                    @Override
                    public void onNext(VideoListInfo info) {
                        filterData(info.getItemList());
                        mRootView.setData(info.getItemList(),pullToRefresh);
                    }
                });
    }
    public void getIndexVideoList(int lastStartId,Boolean isPullRefresh,int page) {
        if (isPullRefresh){
            mModel.getIndexVideoList(lastStartId).compose(RxUtils.applySchedulers(mRootView))
                    .subscribe(new ErrorHandleSubscriber<IndextVideoListInfo>(mErrorHandler) {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            mRootView.showLoading();
                        }

                        @Override
                        public void onNext(IndextVideoListInfo info) {
                            mRootView.setData(getIndexVideoList(info),true);
                        }
                    });
        }else {
            mModel.getMoreIndexVideoList(page).compose(RxUtils.applySchedulers(mRootView))
                    .subscribe(new ErrorHandleSubscriber<IndextVideoListInfo>(mErrorHandler) {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(IndextVideoListInfo info) {
                            mRootView.setData(getIndexVideoList(info),false);
                        }
                    });
        }
    }

    private void filterData(List<VideoListInfo.Video> list) {
        Iterator<VideoListInfo.Video> iterator = list.iterator();
        while(iterator.hasNext()){
            VideoListInfo.Video video = iterator.next();
            if(video.getData().getPlayInfo() == null){
                iterator.remove();
            }
        }
    }
    private List<VideoListInfo.Video> getIndexVideoList(IndextVideoListInfo info) {
        List<VideoListInfo.Video> list = new ArrayList<>();
        for (IndextVideoListInfo.ItemList itemList: info.getItemList()){
            if (!itemList.getType().equals("followCard")){
                continue;
            }
            if (itemList.getData().getContent() ==null){
                continue;
            }
            if (itemList.getData().getContent().getType().equals("video")){
                list.add(itemList.getData().getContent());
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
