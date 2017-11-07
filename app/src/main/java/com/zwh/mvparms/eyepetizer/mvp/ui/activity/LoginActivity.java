package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apt.TRouter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.SharedPreferencesUtils;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

@Router(Constants.LOGIN)
public class LoginActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.edt_account)
    EditText edtAccount;
    @BindView(R.id.edt_password1)
    EditText edtPassword1;
    @BindView(R.id.btn_regist)
    Button btnRegist;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolBar();
        edtAccount.addTextChangedListener(new MyTextWatcher());
        edtPassword1.addTextChangedListener(new MyTextWatcher());
        String accountName = (String) SharedPreferencesUtils.getParam(this,Constants.USER_NAME,"");
        if (!StringUtils.isEmpty(accountName)){
            edtAccount.setText(accountName);
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


    @OnClick({R.id.btn_regist, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_regist:
                TRouter.go(Constants.REGIST);
                break;
            case R.id.btn_login:
                User user = new User();
                user.setUsername(edtAccount.getText().toString());
                user.setPassword(edtPassword1.getText().toString());
                ProgressDialog dialog = ProgressDialog.show(this, "", "正在登录...",
                        false, true);
                user.login(new SaveListener<User>() {

                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null){
                            SharedPreferencesUtils.setParam(LoginActivity.this,Constants.USER_NAME,user.getUsername());
                            SharedPreferencesUtils.setParam(LoginActivity.this,Constants.USER_SESSION_TOKEN,user.getSessionToken());
                            SharedPreferencesUtils.setParam(LoginActivity.this,Constants.USER_FACE_PIC_URL,user.getIcon().getFileUrl());
                            UiUtils.makeText(LoginActivity.this,"登陆成功");
                            EventBus.getDefault().post(user, EventBusTags.SET_USER_INFO);
                            dialog.dismiss();
                            finish();
                        }else{
                            UiUtils.makeText(LoginActivity.this,e.getMessage());
                            dialog.dismiss();
                        }
                    }
                });
                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0 && edtAccount.getText().toString().length() > 0 && edtPassword1.getText().toString().length() > 0) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Subscriber(tag = EventBusTags.SET_USER_INFO)
    public void setUserInfo(User user) {
        edtAccount.setText(user.getUsername());
    }

}
