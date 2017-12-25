package com.zwh.mvparms.eyepetizer.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.contract.CategoryContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class CategoryPresenter extends BasePresenter<CategoryContract.Model, CategoryContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public CategoryPresenter(CategoryContract.Model model, CategoryContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void requestCategories() {
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                mModel.getCategories().compose(RxUtils.applySchedulers(mRootView))
                        .subscribe(new ErrorHandleSubscriber<List<Category>>(mErrorHandler) {
                            @Override
                            public void onNext(List<Category> categories) {
                                mRootView.setData(categories);
                            }
                        });
            }

            @Override
            public void onRequestPermissionFailure() {
                mModel.getCategories().compose(RxUtils.applySchedulers(mRootView))
                        .subscribe(new ErrorHandleSubscriber<List<Category>>(mErrorHandler) {
                            @Override
                            public void onNext(List<Category> categories) {
                                mRootView.setData(categories);
                            }
                        });
                mRootView.showMessage("Request permissons failure");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
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
