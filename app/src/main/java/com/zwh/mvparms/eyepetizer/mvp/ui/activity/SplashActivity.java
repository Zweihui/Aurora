package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.apt.TRouter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerSplashComponent;
import com.zwh.mvparms.eyepetizer.di.module.SplashModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.SplashContract;
import com.zwh.mvparms.eyepetizer.mvp.contract.UserContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.presenter.SplashPresenter;
import com.zwh.mvparms.eyepetizer.mvp.presenter.UserPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.WeatherView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Administrator on 2017/8/21 0021.
 */
@Router(Constants.SPLASH)
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View{
    @BindView(R.id.weather)
    WeatherView weatherView;

    private List<Category> list = new ArrayList<>();

    private boolean isInit = true;
    private RxPermissions mRxPermissions;
    private boolean isDataLoad = false;
    private boolean isAnimated = false;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerSplashComponent.builder()
                .appComponent(appComponent)
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.requestCategories();
    }

    @Override
    public void bind(Bundle savedInstanceState) {
        TRouter.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInit){
            weatherView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    weatherView.startAnim();
                    weatherView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isAnimated = true;
                            EventBus.getDefault().post("gotomain",EventBusTags.JUMP_TO_HOME);
                        }
                    }, 4000);
                }
            }, 200);
            isInit = false;
        }
    }

    @Subscriber(tag = EventBusTags.JUMP_TO_HOME)
    public void goToHomePage(String s) {
        if (isDataLoad && isAnimated ){
            TRouter.go(Constants.HOME,new DataExtra(Constants.SPLASH_DATA, list).build());
            finish();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showMessage(String message) {
        UiUtils.snackbarText(message);
        isDataLoad = true;
        EventBus.getDefault().post("gotomain",EventBusTags.JUMP_TO_HOME);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void setData(List<Category> list) {
        if (!StringUtils.isEmpty(list)){
            this.list.addAll(list);
        }
        isDataLoad = true;
        EventBus.getDefault().post("gotomain",EventBusTags.JUMP_TO_HOME);
    }
}
