package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.SharedPreferencesUtils;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.ui.MatisseActivity;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.MainActivity;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.CircleImageView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.R.attr.name;
import static android.os.Build.VERSION_CODES.M;
import static com.zhihu.matisse.Matisse.obtainPathResult;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class MineFragment extends BaseLazyLoadFragment implements View.OnClickListener {

    private LinearLayout mLlFace;
    private CircleImageView mCivFace;
    private TextView mTvName;
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
        mLlFace = (LinearLayout) view.findViewById(R.id.ll_face);
        mCivFace = (CircleImageView) view.findViewById(R.id.civ_face);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_face:
                requestSelectPic();
                break;
        }
    }

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
        BmobFile file = new BmobFile((String)SharedPreferencesUtils.getParam(getActivity(), Constants.USER_NAME,""),null,path);
        file.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){

                }
            }
        });
//        BmobUser bmobUser = BmobUser.getCurrentUser();
//        User user = new User();
//        user.setIcon(file);
//        user.update(bmobUser.getObjectId(), new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null){
//
//                }
//            }
//        });
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
                    .placeholder(R.drawable.ic_noface)
                    .errorPic(R.drawable.ic_noface)
                    .build());
        }
    }
}
