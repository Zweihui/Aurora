package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.VideoDetailPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.SampleVideo;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.listener.SampleListener;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.model.SwitchVideoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
@Router(Constants.VIDEO)
public class VideoDetailActivity extends BaseActivity<VideoDetailPresenter> {

    @BindView(R.id.rl_screen)
    FrameLayout rlScreen;
//    @BindView(R.id.iv_video_bg)
//    @SceneTransition(Constants.TRANSLATE_VIEW)
//    public ImageView mIvVideoBg;
    @Extra(Constants.VIDEO_INFO)
    public VideoListInfo.Video videoInfo;

    @BindView(R.id.detail_player)
    SampleVideo detailPlayer;

    private boolean isPlay;
    private boolean isPause;

    private OrientationUtils orientationUtils;

    private AppComponent mAppComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_video_detail;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        String source1 = videoInfo.getData().getPlayInfo().get(0).getUrl();
        String name = "标清";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);
        String source2 = videoInfo.getData().getPlayInfo().get(1).getUrl();
        String name2 = "高清";
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);
        List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);
        list.add(switchVideoModel2);
        detailPlayer.setUp(list, true, "");
        //增加封面
        ImageView mIvVideoBg = new ImageView(this);
        mIvVideoBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(videoInfo.getData().getCover().getFeed())
                        .imageView(mIvVideoBg)
                        .transformation(new FitCenter(VideoDetailActivity.this))
                        .build());
        detailPlayer.setThumbImageView(mIvVideoBg);

        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        detailPlayer.setIsTouchWiget(true);
        //detailPlayer.setIsTouchWigetFull(false);
        //关闭自动旋转
        detailPlayer.setRotateViewAuto(false);
        detailPlayer.setLockLand(false);
        detailPlayer.setShowFullAnimation(false);
        detailPlayer.setNeedLockFull(true);
        detailPlayer.setSeekRatio(1);
        //detailPlayer.setOpenPreView(false);
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(VideoDetailActivity.this, true, true);
            }
        });

        detailPlayer.setStandardVideoAllCallBack(new SampleListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
                isPlay = true;
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }
        });

        detailPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }


    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }

}
