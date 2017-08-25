package com.jess.arms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.delegate.IFragment;
import com.jess.arms.mvp.IPresenter;
import com.trello.rxlifecycle2.components.support.RxFragment;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

public abstract class BaseLazyLoadFragment<P extends IPresenter> extends RxFragment implements IFragment {
    protected final String TAG = this.getClass().getSimpleName();

    //是否可见
    protected boolean mIsFirstVisible=true;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;


    @Inject
    protected P mPresenter;


    public BaseLazyLoadFragment() {
        //必须确保在Fragment实例化时setArguments()
        setArguments(new Bundle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPrepareStates();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    /**
     * 实现Fragment数据的缓加载
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    protected void onInVisible() {}

    protected abstract void loadData();

    public void onVisible() {
        if (mIsFirstVisible) {
            mIsFirstVisible = false;
            onPrepareStates();
        }
    }

    private synchronized void onPrepareStates() {
        if (isPrepared) {
            loadData();
        } else {
            isPrepared = true;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mPresenter != null) mPresenter.onDestroy();//释放资源
//        this.mPresenter = null;
    }


    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
        return true;
    }
}
