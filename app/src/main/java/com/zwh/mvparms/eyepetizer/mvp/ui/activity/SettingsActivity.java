package com.zwh.mvparms.eyepetizer.mvp.ui.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apt.TRouter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.DeviceUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.SharedPreferencesUtils;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.beta.Beta;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

@Router(Constants.SETTINGS)
public class SettingsActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.switch_flow)
    Switch switchFlow;
    @BindView(R.id.ctl_flow)
    ConstraintLayout ctlFlow;
    @BindView(R.id.tv_wifi_open)
    TextView tvWifiOpen;
    @BindView(R.id.switch_wifi)
    Switch switchWifi;
    @BindView(R.id.ctl_wifi)
    ConstraintLayout ctlWifi;
    @BindView(R.id.tv_splash_open)
    TextView tvSplashOpen;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.switch_splash)
    Switch switchSplash;
    @BindView(R.id.ctl_splash)
    ConstraintLayout ctlSplash;
    @BindView(R.id.ctl_clear)
    ConstraintLayout ctlClear;
    @BindView(R.id.ctl_author)
    ConstraintLayout ctlAuthor;
    @BindView(R.id.ctl_rate)
    ConstraintLayout ctlRate;
    @BindView(R.id.ctl_open_source)
    ConstraintLayout ctlOpenSource;
    @BindView(R.id.ctl_update)
    ConstraintLayout ctlUpdate;
    @BindView(R.id.ctl_logout)
    ConstraintLayout ctlLogout;

    private RxErrorHandler mErrorHandler;
    private RxPermissions mRxPermissions;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        mRxPermissions = new RxPermissions(this);
        mErrorHandler = appComponent.rxErrorHandler();
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_settings;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolBar();
        switchFlow.setChecked((Boolean) SharedPreferencesUtils.getParam(this,Constants.SETTING_FLOW,true));
        switchWifi.setChecked((Boolean) SharedPreferencesUtils.getParam(this,Constants.SETTING_WIFI,true));
        switchSplash.setChecked((Boolean) SharedPreferencesUtils.getParam(this,Constants.SETTING_SPLASH,false));
        tvVersionName.setText("当前版本"+DeviceUtils.getVersionName(this));
        if (BmobUser.getCurrentUser()==null){
            ctlLogout.setVisibility(View.GONE);
        }
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.ctl_flow, R.id.ctl_wifi, R.id.ctl_splash, R.id.ctl_clear, R.id.ctl_author, R.id.ctl_rate, R.id.ctl_open_source, R.id.ctl_update, R.id.ctl_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctl_flow:
                if (switchFlow.isChecked()){
                    switchFlow.setChecked(false);
                }else {
                    switchFlow.setChecked(true);
                }
                SharedPreferencesUtils.setParam(this,Constants.SETTING_FLOW,switchFlow.isChecked());
                break;
            case R.id.ctl_wifi:
                if (switchWifi.isChecked()){
                    switchWifi.setChecked(false);
                    tvWifiOpen.setText("关");
                }else {
                    switchWifi.setChecked(true);
                    tvWifiOpen.setText("开");
                }
                SharedPreferencesUtils.setParam(this,Constants.SETTING_WIFI,switchWifi.isChecked());
                break;
            case R.id.ctl_splash:
                if (switchSplash.isChecked()){
                    switchSplash.setChecked(false);
                    tvSplashOpen.setText("关");
                }else {
                    switchSplash.setChecked(true);
                    tvSplashOpen.setText("开");
                }
                SharedPreferencesUtils.setParam(this,Constants.SETTING_SPLASH,switchSplash.isChecked());
                break;
            case R.id.ctl_clear:
                PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        if (!DataHelper.findFile(getExternalCacheDir())){
                            UiUtils.makeText(SettingsActivity.this,"暂无缓存");
                            return;
                        }
                        DataHelper.deleteDir(getCacheDir());
                        DataHelper.deleteDir(getExternalCacheDir());
                        UiUtils.makeText(SettingsActivity.this,"缓存已删除");
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissions) {
                        UiUtils.snackbarText("Request permissons failure");
                    }

                    @Override
                    public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                        UiUtils.snackbarText("Request permissons failure");
                    }

                }, mRxPermissions, mErrorHandler);
                break;
            case R.id.ctl_author:
                TRouter.go(Constants.AUTHOR,new DataExtra(Constants.AUTHOR_TYPE,0).build());
                break;
            case R.id.ctl_rate:
                try{
                    Uri uri = Uri.parse("market://details?id="+getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch(Exception e){
                    Toast.makeText(this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.ctl_open_source:
                break;
            case R.id.ctl_update:
                Beta.checkUpgrade();
                break;
            case R.id.ctl_logout:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("确定不是手滑了吗?").setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post("succeed", EventBusTags.SETTING_ACTIVITY_LOG_OUT);
                                BmobUser.logOut();
                                ctlLogout.setVisibility(View.GONE);
                            }
                        }).setNegativeButton("手滑了", null);
                alert.create();
                alert.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxPermissions = null;
    }
}
