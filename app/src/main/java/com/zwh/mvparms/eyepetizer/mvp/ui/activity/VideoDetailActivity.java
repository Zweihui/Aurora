package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.apt.TRouter;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.jess.arms.base.App;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.annotation.apt.SceneTransition;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;


import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
@Router(Constants.VIDEO)
public class VideoDetailActivity extends BaseActivity {

    @BindView(R.id.show_screen)
    TextureView showScreen;
    @BindView(R.id.rl_screen)
    FrameLayout rlScreen;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.iv_video_bg)
    @SceneTransition(Constants.TRANSLATE_VIEW)
    public ImageView mIvVideoBg;
    @Extra(Constants.VIDEO_INFO)
    public VideoListInfo.Video videoInfo;

    public IjkMediaPlayer player = null;
    private AppComponent mAppComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMediaPlayer();
        mAppComponent =  ((App)getApplicationContext())
                .getAppComponent();
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(videoInfo.getData().getCover().getFeed())
                        .imageView(mIvVideoBg)
                        .transformation(new FitCenter(VideoDetailActivity.this))
                        .build());
        mIvVideoBg = (ImageView) findViewById(R.id.iv_video_bg);
    }

    private void initMediaPlayer(){
        player = new IjkMediaPlayer();
        showScreen.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                try {
                    player.setSurface(new Surface(surface));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                IjkMediaPlayer.loadLibrariesOnce(null);
                IjkMediaPlayer.native_profileBegin("libijkplayer.so");
                player.setOnPreparedListener(mPreparedListener);
                try {
                    player.setDataSource(VideoDetailActivity.this
                            , Uri.parse(videoInfo.getData().getPlayInfo().get(0).getUrl()));
                    player.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },3000);
    }

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            player.start();
        }
    };

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_video_detail;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void bind(Bundle savedInstanceState) {
        TRouter.bind(this);
    }
}
