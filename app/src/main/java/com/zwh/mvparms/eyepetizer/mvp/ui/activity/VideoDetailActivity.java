package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.apt.TRouter;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.annotation.apt.SceneTransition;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerVideoDetailComponent;
import com.zwh.mvparms.eyepetizer.di.module.VideoDetailModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.RelateVideoInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.VideoDetailPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.RelateVideoAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.section.RelateVideoSection;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.ExpandTextView;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.SampleVideo;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.listener.SampleListener;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.model.SwitchVideoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Router(Constants.VIDEO)
public class VideoDetailActivity extends BaseActivity<VideoDetailPresenter> implements VideoDetailContract.View{

    @BindView(R.id.rl_screen)
    FrameLayout rlScreen;
    @Extra(Constants.VIDEO_INFO)
    public VideoListInfo.Video videoInfo;
    @SceneTransition(Constants.TRANSLATE_VIEW)
    @BindView(R.id.detail_player)
    public SampleVideo detailPlayer;
    @BindView(R.id.iv_blur_bg)
    ImageView ivBlurBg;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RelateVideoAdapter adapter;
    private List<RelateVideoSection> datas = new ArrayList<>();

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
        DaggerVideoDetailComponent
                .builder()
                .videoDetailModule(new VideoDetailModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_video_detail;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        initMedia();
        initRecyclerView();
        mPresenter.getRelaRelateVideoInfo(videoInfo.getData().getId());
    }

    private void initRecyclerView() {
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(videoInfo.getData().getCover().getBlurred())
                        .imageView(ivBlurBg)
                        .transformation(new FitCenter(VideoDetailActivity.this))
                        .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter =new RelateVideoAdapter(R.layout.item_video_detail_group_content,
                R.layout.item_video_detail_group_head,datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (datas.get(position).isHeader)
                    return;
                TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, datas.get(position).t.getData()).build());
            }
        });
        View headView = getLayoutInflater().inflate(R.layout.item_video_detail_top, recyclerView, false);
        adapter.addHeaderView(headView);
        recyclerView.setAdapter(adapter);
        initHeadView(headView);

    }

    private void initHeadView(View headView) {
        ExpandTextView expand = ((ExpandTextView)headView.findViewById(R.id.tv_expand));
        expand.setTextMaxLine(2);
        expand.setText(videoInfo.getData().getDescription());
        ImageView expandArrow = ((ImageView)headView.findViewById(R.id.iv_expand));
        expand.post(new Runnable() {
            @Override
            public void run() {
                if (!expand.canExpand()){
                    expandArrow.setVisibility(View.GONE);
                }
            }
        });
        ((TextView)headView.findViewById(R.id.tv_title)).setText(videoInfo.getData().getTitle());
        ((TextView)headView.findViewById(R.id.tv_type)).setText(getDetailStr(videoInfo));
        expandArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expand.isExpand()){
                    expand.shrink();
                    expandArrow.setImageResource(R.drawable.up_enter_arrow);
                }else {
                    expand.expand();
                    expandArrow.setImageResource(R.drawable.down_enter_arrow);
                }
            }
        });
        ((TextView)headView.findViewById(R.id.tv_collection)).setText(videoInfo.getData().getConsumption().getCollectionCount()+"");
        ((TextView)headView.findViewById(R.id.tv_upload)).setText(videoInfo.getData().getConsumption().getShareCount()+"");
        ((TextView)headView.findViewById(R.id.tv_comment)).setText(videoInfo.getData().getConsumption().getReplyCount()+"");
        if (videoInfo.getData().getAuthor()!=null){
            ((TextView)headView.findViewById(R.id.tv_author_name)).setText(videoInfo.getData().getAuthor().getName());
            ((TextView)headView.findViewById(R.id.tv_author_des)).setText(videoInfo.getData().getAuthor().getDescription());
            mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                            ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                    GlideImageConfig
                            .builder()
                            .url(videoInfo.getData().getCover().getFeed())
                            .imageView(((ImageView)headView.findViewById(R.id.iv_author)))
                            .transformation(new GlideCircleTransform(VideoDetailActivity.this))
                            .build());
        }
    }

    private void initMedia() {
        detailPlayer.setEnlargeImageRes(R.drawable.ve_video_full_screen); //设置全屏按钮
        detailPlayer.setShrinkImageRes(R.drawable.ve_exit_full_screen); //设置缩屏按钮
        try {
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
        }catch (IndexOutOfBoundsException e) {
            String source1 = videoInfo.getData().getPlayUrl();
            String name = "标清";
            SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);
            String source2 = videoInfo.getData().getPlayUrl();
            String name2 = "高清";
            SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);
            List<SwitchVideoModel> list = new ArrayList<>();
            list.add(switchVideoModel);
            list.add(switchVideoModel2);
            detailPlayer.setUp(list, true, "");
        }
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

    @Override
    public void setData(RelateVideoInfo info) {
        for (RelateVideoInfo.ItemListBean item:info.getItemList()){
            RelateVideoSection section = new RelateVideoSection(item);
            if (item.getType().equals("textCard")){
                section.isHeader = true;
            }else {
                section.isHeader = false;
            }
            datas.add(section);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    private String getDetailStr(VideoListInfo.Video item){
        String duration = item.getData().getDuration()+"";
        int seconds = Integer.parseInt(duration);
        int temp=0;
        StringBuffer sb=new StringBuffer();
        temp = seconds/3600;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600/60;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600%60;
        sb.append((temp<10)?"0"+temp:""+temp);
        String detail = "#"+item.getData().getCategory()+" / "+sb.toString();
        return detail;
    }

}
