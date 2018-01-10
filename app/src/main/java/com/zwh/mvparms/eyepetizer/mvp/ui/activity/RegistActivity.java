package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

@Router(Constants.REGIST)
public class RegistActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_district)
    TextView tvDistrict;
    @BindView(R.id.error_msg)
    TextView tvErrorMsg;
    @BindView(R.id.rl_select_district)
    RelativeLayout rlSelectDistrict;
    @BindView(R.id.tv_district_num)
    TextView tvDistrictNum;
    @BindView(R.id.ev_phone_num)
    EditText evPhoneNum;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.tv_pwd_error)
    TextView tvPwdError;
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode;
    @BindView(R.id.edt_password1)
    EditText etPassword1;
    @BindView(R.id.edt_password2)
    EditText etPassword2;
    @BindView(R.id.btn_reget_code)
    Button btnRegetCode;
    @BindView(R.id.btn_verification)
    Button btnVerification;

    private RxPermissions rxPermissions;
    private RxErrorHandler mErrorHandler;

    private int mSmsId;
    private int step = 1;

    private static final String[] DISTRICT_NAME = {"中国大陆", "香港特别行政区", "澳门特别行政区", "台湾地区"
            , "美国", "比利时", "澳大利亚", "法国", "加拿大", "日本", "新加坡", "韩国", "马来西亚", "英国", "意大利"
            , "德国", "俄罗斯", "新西兰"};
    private static final String[] DISTRICT_NUM = {"+86", "+852", "+853", "+886", "+1", "+32", "+61", "+33", "+1", "+81",
            "+65", "+82", "+60", "+44", "+39", "+49", "+7", "+86", "+64"};


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        rxPermissions = new RxPermissions(this);
        mErrorHandler = appComponent.rxErrorHandler();
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnRegetCode.setText((millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            btnRegetCode.setEnabled(true);
            btnRegetCode.setText("获取验证码");
        }
    };

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_regist;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        BmobSMS.initialize(this, Constants.BMOB_APP_ID);
        initToolBar();
        evPhoneNum.addTextChangedListener(new MyTextWatcher());
        etVerificationCode.addTextChangedListener(new MyTextWatcher());
        etPassword1.addTextChangedListener(new MyTextWatcher());
        etPassword2.addTextChangedListener(new MyTextWatcher());
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backpress();
            }
        });
    }

    @OnClick({R.id.rl_select_district, R.id.btn_reget_code, R.id.btn_verification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_select_district:
                showSelectDistrictDialog();
                break;
            case R.id.btn_reget_code:
                requestPermission(false);
                break;
            case R.id.btn_verification:
                UiUtils.hidesoftInput(this, mToolbar);
                btnNextPressed();
                break;
        }
    }

    private void showSelectDistrictDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("地区选择");
        dialog.setSingleChoiceItems(DISTRICT_NAME, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvDistrictNum.setText(DISTRICT_NUM[which]);
                tvDistrict.setText(DISTRICT_NAME[which]);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.show();

    }

    private boolean isPhoneNumOk() {
        if (!StringUtils.isMobile(evPhoneNum.getText().toString())) {
            tvErrorMsg.setTextColor(Color.RED);
            tvErrorMsg.setText("手机号码格式不正确");
            return false;
        } else {
            return true;
        }
    }

    private void btnNextPressed() {
        switch (step) {
            case 1:
                if (!isPhoneNumOk()) {
                    return;
                }
                requestPermission(true);
                break;
            case 2:
                ProgressDialog dialog2 = ProgressDialog.show(this, "", "正在验证...",
                        false, true);
                BmobSMS.verifySmsCode(this, evPhoneNum.getText().toString(), etVerificationCode.getText().toString(), new VerifySMSCodeListener() {

                    @Override
                    public void done(BmobException ex) {
                        // TODO Auto-generated method stub
                        if (ex == null) {//短信验证码已验证成功
                            gotoStep(3,false);
                        } else {
                            UiUtils.makeText(RegistActivity.this,ex.getMessage());
                        }
                        dialog2.dismiss();
                    }
                });
                break;
            case 3:
                if (!etPassword1.getText().toString().equals(etPassword2.getText().toString())){
                    tvPwdError.setText("两次密码不一致");
                    tvPwdError.setTextColor(Color.RED);
                    return;
                }
                User user = new User();
                user.setUsername(evPhoneNum.getText().toString());
                user.setPassword(etPassword2.getText().toString());
                ProgressDialog dialog3 = ProgressDialog.show(this, "", "正在注册...",
                        false, true);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User u, cn.bmob.v3.exception.BmobException e) {
                        if (e == null){
                            UiUtils.makeText(RegistActivity.this,"注册成功");
                            timer.cancel();
                            timer = null;
                            finish();
                        }else {
                            UiUtils.makeText(RegistActivity.this,e.getMessage());
                        }
                        dialog3.dismiss();
                    }
                });
                break;
        }
    }

    private void gotoStep(int step,boolean isFromBack) {
        this.step = step;
        if (step == 1) {
            findViewById(R.id.include_step2).setVisibility(View.GONE);
            findViewById(R.id.include_step3).setVisibility(View.GONE);
            findViewById(R.id.include_step1).setVisibility(View.VISIBLE);
            btnVerification.setText("获取验证码");
            setTitle("注册帐号");
            if (isFromBack){
                tvErrorMsg.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                tvErrorMsg.setText("请确认你的国家或地区并输入手机号");
                btnVerification.setEnabled(true);
            }else {
                btnVerification.setEnabled(false);
            }
        } else if (step == 2) {
            findViewById(R.id.include_step1).setVisibility(View.GONE);
            findViewById(R.id.include_step2).setVisibility(View.VISIBLE);
            findViewById(R.id.include_step3).setVisibility(View.GONE);
            btnVerification.setText("下一步");
            setTitle("确认验证码");
            tvPhoneNum.setText(tvDistrictNum.getText().toString() + evPhoneNum.getText().toString());
            if (isFromBack){
                btnVerification.setEnabled(true);
            }else {
                etVerificationCode.setText("");
                btnRegetCode.setEnabled(false);
                btnVerification.setEnabled(false);
                timer.start();
            }
        } else {
            findViewById(R.id.include_step1).setVisibility(View.GONE);
            findViewById(R.id.include_step2).setVisibility(View.GONE);
            findViewById(R.id.include_step3).setVisibility(View.VISIBLE);
            btnVerification.setText("完成");
            setTitle("设置登录密码");
            if (isFromBack){

            }else {
                tvPwdError.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                tvPwdError.setText("请输入登录密码");
                etPassword1.setText("");
                etPassword2.setText("");
                btnVerification.setEnabled(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        backpress();
    }

    private void backpress() {
        UiUtils.hidesoftInput(this, mToolbar);
        if (findViewById(R.id.include_step3).getVisibility() == View.VISIBLE) {
            gotoStep(2,true);
        } else if (findViewById(R.id.include_step2).getVisibility() == View.VISIBLE) {
            gotoStep(1,true);
        } else {
            timer.cancel();
            timer = null;
            finish();
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (step < 3) {
                if (s.toString().length() > 0) {
                    btnVerification.setEnabled(true);
                } else {
                    btnVerification.setEnabled(false);
                }
            } else {
                if (s.toString().length() > 0 && etPassword1.getText().toString().length() > 0 && etPassword2.getText().toString().length() > 0) {
                    btnVerification.setEnabled(true);
                } else {
                    btnVerification.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void requestPermission(boolean shouldGoNext){
        PermissionUtil.readPhonestate(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                ProgressDialog dialog = ProgressDialog.show(RegistActivity.this, "", "正在获取验证码...",
                        false, true);
                BmobQuery<User> bmobQuery = new BmobQuery<User>();
                bmobQuery.addWhereEqualTo("username",evPhoneNum.getText().toString());
                bmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, cn.bmob.v3.exception.BmobException e) {
                        if (StringUtils.isEmpty(list)){
                            BmobSMS.requestSMSCode(RegistActivity.this, evPhoneNum.getText().toString(), "regist", new RequestSMSCodeListener() {

                                @Override
                                public void done(Integer smsId, BmobException ex) {
                                    // TODO Auto-generated method stub
                                    if (ex == null) {//验证码发送成功
                                        mSmsId = smsId;
                                        UiUtils.makeText(RegistActivity.this,"验证码发送成功");
                                        if (shouldGoNext){
                                            gotoStep(2,false);
                                        }
                                    } else {
                                        UiUtils.makeText(RegistActivity.this,"验证码发送失败");
                                    }
                                    if (!RegistActivity.this.isFinishing())
                                        dialog.dismiss();
                                }
                            });
                            btnRegetCode.setEnabled(false);
                            timer.start();
                        }else {
                            dialog.dismiss();
                            tvPwdError.setTextColor(Color.RED);
                            tvErrorMsg.setText("帐号已存在，请不要重复注册！");
                        }
                    }
                });
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                UiUtils.makeText(RegistActivity.this,"权限被拒绝");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                UiUtils.makeText(RegistActivity.this,"权限被拒绝");
            }

        }, rxPermissions, mErrorHandler);
    }
}
