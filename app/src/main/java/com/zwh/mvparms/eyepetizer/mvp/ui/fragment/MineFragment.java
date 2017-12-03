package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apt.TRouter;
import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zwh.annotation.aspect.CheckLogin;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.CircleImageView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class MineFragment extends BaseLazyLoadFragment implements View.OnClickListener {

    private CardView mLlFace;
    private CircleImageView mCivFace;
    private TextView mTvName;
    private LinearLayout mLlAttention;
    private LinearLayout mLlCache;
    private LinearLayout mLlRecord;
    private LinearLayout mLlFeedBack;
    public static final int REQUEST_CODE_CHOOSE = 11;
    AppComponent appComponent;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_mine, container, false);
        mLlFace = (CardView) view.findViewById(R.id.ll_face);
        mCivFace = (CircleImageView) view.findViewById(R.id.civ_face);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mLlCache = (LinearLayout) view.findViewById(R.id.ll_cache);
        mLlAttention = (LinearLayout) view.findViewById(R.id.ll_attention);
        mLlFeedBack = (LinearLayout) view.findViewById(R.id.ll_feedback);
        mLlRecord = (LinearLayout) view.findViewById(R.id.ll_record);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void setData(Object data) {

    }

    @Override
    protected void loadData() {
        mLlFace.setOnClickListener(this);
        mLlCache.setOnClickListener(this);
        mLlAttention.setOnClickListener(this);
        mLlFeedBack.setOnClickListener(this);
        mLlRecord.setOnClickListener(this);
    }

    @Override
    @SingleClick
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_face:
                requestSelectPic();
                break;
            case R.id.ll_record:
                TRouter.go(Constants.HISTORY);
                break;
            case R.id.ll_cache:
                TRouter.go(Constants.CACHE);
                break;
            case R.id.ll_attention:
                gotoMyAttention();
                break;
            case R.id.ll_feedback:
                break;
        }
    }

    @CheckLogin
    private void gotoMyAttention(){
        TRouter.go(Constants.MYATTENTION);
    }
    @CheckLogin
    private void requestSelectPic(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("前往更换头像").setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post("succeed", EventBusTags.MAIN_ACTIVITY_PERMISSION);
                    }
                }).setNegativeButton("取消", null);
        alert.create();
        alert.show();
    }


    @Subscriber(tag = EventBusTags.MINE_FRAGMENT_PERMISSION_BACK)
    private void gotoSelectPic(String tag){
        File tempFile = new File(Environment.getExternalStorageDirectory(),
                "test.jpg");
        Uri uri = Uri.fromFile(tempFile);
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setAllowedGestures(UCropActivity.SCALE,UCropActivity.SCALE,UCropActivity.SCALE);
        options.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        Matisse.from(getActivity())
                .choose(MimeType.ofImage())
                .countable(false)
                .maxSelectable(1)
                .theme(R.style.AppTheme_selectPic)
                .crop(true)
                .cropOptions(options)
                .cropUri(uri)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zwh.mvparms.eyepetizer.fileprovider"))
                .theme(R.style.AppTheme_selectPic)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
    @Subscriber(tag = EventBusTags.MINE_FRAGMENT_SET_FACE_PIC)
    private void setFacePic(String path){
        BmobUser bmobUser = BmobUser.getCurrentUser();
        BmobFile file = new BmobFile(new File(path));
        file.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    User user = new User();
                    user.setIcon(file);
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){

                            }
                        }
                    });
                }
            }
        });
        appComponent.imageLoader().loadImage(getActivity(), GlideImageConfig
                .builder()
                .url(path)
                .imageView(mCivFace)
                .build());
    }
    @Subscriber(tag = EventBusTags.SET_USER_INFO)
    public void setUserInfo(User user) {
        mTvName.setText(user.getUsername());
        if (user.getIcon() != null){
            BmobFile file = user.getIcon();
            appComponent.imageLoader().loadImage(getActivity(), GlideImageConfig
                    .builder()
                    .url(file.getFileUrl())
                    .imageView(mCivFace)
                    .errorPic(R.drawable.ic_noface)
                    .build());
        }
    }
    @Subscriber(tag = EventBusTags.SETTING_ACTIVITY_LOG_OUT)
    public void logoutReset(String tag) {
        mTvName.setText("未登录");
        appComponent.imageLoader().loadImage(getActivity(), GlideImageConfig
                    .builder()
                    .load(R.drawable.ic_noface)
                    .placeholder(R.drawable.ic_noface)
                    .errorPic(R.drawable.ic_noface)
                    .imageView(mCivFace)
                    .build());
    }
}
