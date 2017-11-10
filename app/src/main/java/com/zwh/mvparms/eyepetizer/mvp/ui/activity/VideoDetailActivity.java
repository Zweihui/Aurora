package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.AnimationUtils;
import com.jess.arms.utils.DeviceUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.SharedPreferencesUtils;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.avi.AVLoadingIndicatorView;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.annotation.apt.SceneTransition;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.di.component.DaggerVideoDetailComponent;
import com.zwh.mvparms.eyepetizer.di.module.VideoDetailModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DaoMaster;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ReplyInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDaoEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.VideoDetailPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.RelateVideoAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.ReplyAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.section.RelateVideoSection;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.section.ReplySection;
import com.zwh.mvparms.eyepetizer.mvp.ui.receiver.NetBroadcastReceiver;
import com.zwh.mvparms.eyepetizer.mvp.ui.service.DownLoadService;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.DragBottomView;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.ExpandTextView;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.MultiRecyclerView;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.SampleVideo;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.listener.SampleListener;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.video.model.SwitchVideoModel;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

@Router(Constants.VIDEO)
public class VideoDetailActivity extends BaseActivity<VideoDetailPresenter> implements VideoDetailContract.View {

    @BindView(R.id.rl_screen)
    FrameLayout rlScreen;
    @Extra(Constants.VIDEO_INFO)
    public VideoListInfo.Video videoInfo;
    @SceneTransition(Constants.TRANSLATE_VIEW)
    @BindView(R.id.detail_player)
    public SampleVideo detailPlayer;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.second_recyclerView)
    MultiRecyclerView recyclerView2;
    @BindView(R.id.dbv_drag)
    DragBottomView dragBottomView;
    @BindView(R.id.reply_recyclerView)
    MultiRecyclerView replyRecyclerView2;
    @BindView(R.id.dbv_drag_reply)
    DragBottomView replyDragBottomView;
    @BindView(R.id.fl_loading)
    FrameLayout flLoading;
    @BindView(R.id.indicator_loading)
    AVLoadingIndicatorView indicatorView;

    private ImageView mIvVideoBg; //视频封面
    private View headView;
    private View footView;

    private RelateVideoAdapter adapter;
    private RelateVideoAdapter secondAdapter;
    private ReplyAdapter replyAdapter;
    private List<RelateVideoSection> datas = new ArrayList<>();
    private List<RelateVideoSection> secondDatas = new ArrayList<>();
    private ShareInfo shareInfo;
    private List<ReplySection> replyDatas = new ArrayList<>();

    private boolean isPlay;
    private boolean isPause;
    private int selectPosition;
    private String path;
    private int id;

    private OrientationUtils orientationUtils;

    private AppComponent mAppComponent;
    private Gson gson;
    private RxPermissions mRxPermissions;
    private DownLoadService downloadService;
    private NetBroadcastReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        mAppComponent = appComponent;
        gson = mAppComponent.gson();
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
        mPresenter.getShareInfo(videoInfo.getData().getId());
        indicatorView.show();
        flLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setSwipeBackEnable(true);
        setEdgeEnable(false);
    }

    private void initRecyclerView() {
        recyclerView2.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        replyRecyclerView2.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        adapter = new RelateVideoAdapter(R.layout.item_video_detail_group_content,
                R.layout.item_video_detail_group_head, datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectPosition = position;
                if (datas.get(position).isHeader) {
                    try {
                        String[] arr = StringUtils.getUrlDecodePath
                                (datas.get(position).t.getData().getActionUrl()).split("\\?");
                        path = arr[0];
                        id = Integer.parseInt(arr[1].split("=")[1]);
                        secondDatas.clear();
                        if (StringUtils.isEmpty(secondDatas)){
                            mPresenter.getSecondRelaRelateVideoInfo(path, id, secondDatas.size());
                        }
                        startAnimate(dragBottomView,true);
                    } catch (Exception e) {

                    }
                } else {
                    flLoading.setVisibility(View.VISIBLE);
                    saveVideoHistory(videoInfo.clone());
                    videoInfo = datas.get(position).t;
                    mPresenter.getRelaRelateVideoInfo(videoInfo.getData().getId());
                    setVideoInfo();
                    detailPlayer.startPlayLogic();
                }
            }
        });
        headView = getLayoutInflater().inflate(R.layout.item_video_detail_top, recyclerView, false);
        footView = getLayoutInflater().inflate(R.layout.item_video_detail_foot, recyclerView, false);
        adapter.addHeaderView(headView);
        adapter.addFooterView(footView);
        recyclerView.setAdapter(adapter);
        refreshHeadView(headView);
    }

    private void refreshHeadView(View headView) {
        ExpandTextView expand = ((ExpandTextView) headView.findViewById(R.id.tv_expand));
        expand.setTextMaxLine(2);
        expand.setText(videoInfo.getData().getDescription());
        ImageView expandArrow = ((ImageView) headView.findViewById(R.id.iv_expand));
        expand.post(new Runnable() {
            @Override
            public void run() {
                if (!expand.canExpand()) {
                    expandArrow.setVisibility(View.GONE);
                }
            }
        });
        ((TextView) headView.findViewById(R.id.tv_title)).setText(videoInfo.getData().getTitle());
        ((TextView) headView.findViewById(R.id.tv_type)).setText(getDetailStr(videoInfo));
        expandArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand.isExpand()) {
                    expand.shrink();
                    expandArrow.setImageResource(R.drawable.ve_down_enter_arrow);
                } else {
                    expand.expand();
                    expandArrow.setImageResource(R.drawable.ve_up_enter_arrow);
                }
            }
        });
        ((TextView) headView.findViewById(R.id.tv_collection)).setText(videoInfo.getData().getConsumption().getCollectionCount() + "");
        ((TextView) headView.findViewById(R.id.tv_upload)).setText(videoInfo.getData().getConsumption().getShareCount() + "");
        ((TextView) headView.findViewById(R.id.tv_comment)).setText(videoInfo.getData().getConsumption().getReplyCount() + "");
        if (videoInfo.getData().getAuthor() != null) {
            ((TextView) headView.findViewById(R.id.tv_author_name)).setText(videoInfo.getData().getAuthor().getName());
            ((TextView) headView.findViewById(R.id.tv_author_des)).setText(videoInfo.getData().getAuthor().getDescription());
            mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                            ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                    GlideImageConfig
                            .builder()
                            .url(videoInfo.getData().getCover().getFeed())
                            .imageView(((ImageView) headView.findViewById(R.id.iv_author)))
                            .transformation(new GlideCircleTransform(VideoDetailActivity.this))
                            .build());
        }
        headView.findViewById(R.id.ll_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimate(replyDragBottomView,true);
                mPresenter.getReplyInfo(videoInfo.getData().getId());
            }
        });
        headView.findViewById(R.id.ll_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoShare();
            }
        });
        headView.findViewById(R.id.ll_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        checkDownload(v,videoInfo);
                    }

                    @Override
                    public void onRequestPermissionFailure() {
                        showMessage("Request permissons failure");
                    }
                }, mRxPermissions, mAppComponent.rxErrorHandler());
            }
        });
        headView.findViewById(R.id.ll_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void downloadVideo() {
        Intent intent = new Intent(this,DownLoadService.class);
        VideoDownLoadInfo info = new VideoDownLoadInfo();
        info.setVideo(videoInfo.getData());
        info.setBody(gson.toJson(videoInfo.getData()));
        info.setFinish(false);
        info.setCreatTime(new Date());
        info.setId(Long.parseLong(videoInfo.getData().getId()+""));
        intent.putExtra(DownLoadService.VIDEOS_INFO,info);
        if (DeviceUtils.isServiceRunning(this,"com.zwh.mvparms.eyepetizer.mvp.ui.service.DownLoadService")){
            Timber.e("-------------PostSticky");
            EventBus.getDefault().postSticky(info,EventBusTags.CACHE_DOWNLOAD_BEGIN);
        }
        startService(intent);
        Snackbar.make(rlScreen, "视频离线缓存中",Snackbar.LENGTH_LONG)
                .setAction("查看任务", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VideoDetailActivity.this, CacheActivity.class);
                        intent.putExtra(CacheActivity.FROM_NOTIFICATION,true);
                        startActivity(intent);
                    }
                })
                .show();
    }


    private void initMedia() {
        detailPlayer.setEnlargeImageRes(R.drawable.ve_video_full_screen); //设置全屏按钮
        detailPlayer.setShrinkImageRes(R.drawable.ve_exit_full_screen); //设置缩屏按钮
        detailPlayer.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.progress_video));
        detailPlayer.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.progress_video), getResources().getDrawable(R.drawable.video_seek_thumb));
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
        } catch (IndexOutOfBoundsException e) {
            String source1 = videoInfo.getData().getPlayUrl();
            String name = "标清";
            SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);
            String source2 = videoInfo.getData().getPlayUrl();
            String name2 = "高清";
            SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);
            List<SwitchVideoModel> list = new ArrayList<>();
            list.add(switchVideoModel);
            list.add(switchVideoModel2);
            detailPlayer.setUp(list, true,"");
        }
        //增加封面
        mIvVideoBg = new ImageView(this);
        mIvVideoBg.setScaleType(ImageView.ScaleType.CENTER_CROP);

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
        if ((Boolean) SharedPreferencesUtils.getParam(this,Constants.SETTING_WIFI,true)){
            detailPlayer.startPlayLogic();
        }
    }


    @Override
    public void onBackPressed() {
        if (dragBottomView.getVisibility() == View.VISIBLE) {
            startAnimate(dragBottomView,false);
            return;
        }
        if (replyDragBottomView.getVisibility() == View.VISIBLE) {
            startAnimate(replyDragBottomView,false);
            return;
        }

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
        detailPlayer.onVideoPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailPlayer.onVideoResume();
        if (receiver == null){
            receiver = new NetBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onDestroy() {
        GSYVideoPlayer.releaseAllVideos();
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
        super.onDestroy();
    }

    private void setVideoInfo() {
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
        } catch (IndexOutOfBoundsException e) {
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
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(videoInfo.getData().getCover().getFeed())
                        .imageView(mIvVideoBg)
                        .transformation(new FitCenter(VideoDetailActivity.this))
                        .build());
        detailPlayer.setThumbImageView(mIvVideoBg);
    }


    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void setData(VideoListInfo info, boolean isShowSecond) {
        if (StringUtils.isEmpty(info.getItemList())) {
            return;
        }
        if (isShowSecond) {
            if (secondAdapter == null) {
                secondAdapter = new RelateVideoAdapter(R.layout.item_video_detail_group_content,
                        R.layout.item_video_detail_group_head, secondDatas);
                View headView = getLayoutInflater().inflate(R.layout.item_video_detail_second_top, recyclerView2, false);
                ((TextView) headView.findViewById(R.id.tv_name)).setText(datas.get(selectPosition).t.getData().getText());
                ((ImageView) headView.findViewById(R.id.iv_arrow)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startAnimate(dragBottomView,false);
                    }
                });
                secondAdapter.setHeaderView(headView);
                secondAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        startAnimate(dragBottomView,false);
                        saveVideoHistory(videoInfo.clone());
                        videoInfo = secondDatas.get(position).t;
                        mPresenter.getRelaRelateVideoInfo(videoInfo.getData().getId());
                        setVideoInfo();
                        detailPlayer.startPlayLogic();
                    }
                });
                secondAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        recyclerView2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.getSecondRelaRelateVideoInfo(path, id, secondDatas.size());
                            }
                        }, 500);
                    }
                }, recyclerView2.getRecyclerView());
                recyclerView2.getRecyclerView().setAdapter(secondAdapter);
            }
            List<RelateVideoSection> newData = new ArrayList<>();
            boolean isrefresh = true;
            if(StringUtils.isEmpty(secondDatas)){
                isrefresh = true;
            }else {
                isrefresh = false;
            }
            for (VideoListInfo.Video item : info.getItemList()) {
                RelateVideoSection section = new RelateVideoSection(item);
                section.isHeader = false;
                newData.add(section);
                secondDatas.add(section);
            }
            if (isrefresh){
                secondAdapter.setNewData(newData);
                ((TextView)secondAdapter.getHeaderLayout().findViewById(R.id.tv_name)).setText(datas.get(selectPosition).t.getData().getText());
            }else {
                secondAdapter.addData(newData);
                secondAdapter.loadMoreComplete();
            }
        } else {
            datas.clear();
            for (VideoListInfo.Video item : info.getItemList()) {
                RelateVideoSection section = new RelateVideoSection(item);
                if (item.getType().equals("textCard")) {
                    section.isHeader = true;
                } else {
                    section.isHeader = false;
                }
                datas.add(section);
            }
            refreshHeadView(headView);
            adapter.setNewData(datas);
            flLoading.postDelayed(new Runnable() {
                @Override
                public void run() {
                    flLoading.setVisibility(View.GONE);
                }
            }, 600);
        }
    }

    @Override
    public void setReplyData(ReplyInfo info, boolean isLoadmore) {
        if (StringUtils.isEmpty(info.getItemList())) {
            return;
        }
        if (replyAdapter == null) {
            replyAdapter = new ReplyAdapter(R.layout.item_reply_content, R.layout.item_video_detail_group_head, replyDatas);
            replyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.iv_arrow_right){
                        startAnimate(replyDragBottomView,false);
                    }
                }
            });
            replyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.iv_arrow_right){
                        startAnimate(dragBottomView,false);
                    }
                }
            });
        }
        if (info.getNextPageUrl() != null) {
            replyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (!StringUtils.isEmpty(replyDatas))
                        mPresenter.getMoreReplyInfo(replyDatas.get(replyDatas.size() - 1).t.getData().getSequence(), videoInfo.getData().getId());
                }
            }, replyRecyclerView2.getRecyclerView());
        } else {
            View footView2 = getLayoutInflater().inflate(R.layout.item_video_detail_foot, replyRecyclerView2, false);
            if (replyAdapter.getFooterLayoutCount() == 0) {
                replyAdapter.addFooterView(footView2);
            }
            replyAdapter.setOnLoadMoreListener(null, replyRecyclerView2.getRecyclerView());
        }
        if (isLoadmore) {
            List<ReplySection> replies = new ArrayList<>();
            for (ReplyInfo.Reply item : info.getItemList()) {
                ReplySection section = new ReplySection(item);
                if (!item.getType().equals("reply")) {
                    section.isHeader = true;
                } else {
                    section.isHeader = false;
                }
                replies.add(section);
            }
            replyDatas.addAll(replies);
            replyAdapter.addData(replies);
            replyAdapter.loadMoreComplete();
        } else {
            replyDatas.clear();
            replyRecyclerView2.getRecyclerView().setAdapter(replyAdapter);

            for (ReplyInfo.Reply item : info.getItemList()) {
                ReplySection section = new ReplySection(item);
                if (!item.getType().equals("reply")) {
                    section.isHeader = true;
                } else {
                    section.isHeader = false;
                }
                replyDatas.add(section);
            }
        }
    }

    @Override
    public void setShareData(ShareInfo info) {
        shareInfo = info;
    }

    private void gotoShare() {
        if (shareInfo == null)
            return;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, shareInfo.getWechat_friends().getLink());
        intent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    @Override
    public void showLoading() {
        recyclerView2.setLoading(true);
        replyRecyclerView2.setLoading(true);
    }

    @Override
    public void hideLoading() {
        recyclerView2.setLoading(false);
        replyRecyclerView2.setLoading(false);
    }

    @Subscriber(tag = EventBusTags.HIDE_RECYCLERVIEW)
    public void startAnimate(View view,boolean isShow) {
        if (isShow) {
            AnimationUtils.startTranslate(view, 0, dragBottomView.getHeight()
                    , 0, 0, 200, true);
        } else {
            AnimationUtils.startTranslate(view, 0, 0
                    , 0, dragBottomView.getHeight(), 200, false);
        }
    }

    @Subscriber(tag = EventBusTags.NET_CHANGE_FLOW)
    private void netChangeToFlow(String tag){
        if ((Boolean) SharedPreferencesUtils.getParam(this,Constants.SETTING_FLOW,true)){
            detailPlayer.onVideoPause();
            UiUtils.makeText(this,"当前正在使用流量");
        }
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

    private String getDetailStr(VideoListInfo.Video item) {
        String duration = item.getData().getDuration() + "";
        int seconds = Integer.parseInt(duration);
        int temp = 0;
        StringBuffer sb = new StringBuffer();
        temp = seconds / 3600;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = seconds % 3600 / 60;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = seconds % 3600 % 60;
        sb.append((temp < 10) ? "0" + temp : "" + temp);
        String detail = "#" + item.getData().getCategory() + " / " + sb.toString();
        return detail;
    }
    @SingleClick
    private void checkDownload(View view,VideoListInfo.Video videoInfo){
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            DaoMaster master = GreenDaoHelper.getInstance().create("DOWNLOAD").getMaster();
            VideoDownLoadInfo video = master.newSession().getVideoDownLoadInfoDao().loadByRowId(videoInfo.getData().getId());
            if(video==null){
                e.onNext(false);
            }else {
                e.onNext(true);
            }
            e.onComplete();
        }).compose(RxUtils.applySchedulersWithLifeCycle(VideoDetailActivity.this))
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(@NonNull Boolean aBoolean) throws Exception {
                                   if (aBoolean){
                                       UiUtils.makeText(VideoDetailActivity.this,"视频已在缓存列表中");
                                   }else {
                                       downloadVideo();
                                   }
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        }
                );
    }

    private void saveVideoHistory(VideoListInfo.Video videoInfo) {
        VideoDaoEntity daoEntity = new VideoDaoEntity();
        daoEntity.setId(new Long((long)videoInfo.getData().getId()));
        daoEntity.setBody(gson.toJson(videoInfo.getData()));
        daoEntity.setDate(new Date());
        if (shareInfo != null){
            if (shareInfo.getWechat_friends()!=null){
                daoEntity.setShareInfo(shareInfo.getWechat_friends().getLink());
            }
        }
        daoEntity.setStartTime(detailPlayer.getCurrentPositionWhenPlaying());
        daoEntity.setTotalTime(videoInfo.getData().getDuration());
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null){
            daoEntity.setUserId(user.getObjectId());
        }
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            if (user != null){
                BmobQuery<VideoDaoEntity> bmobQuery = new BmobQuery<VideoDaoEntity>();
                bmobQuery.addWhereEqualTo("id",daoEntity.getId());
                bmobQuery.findObjects(new FindListener<VideoDaoEntity>() {
                    @Override
                    public void done(List<VideoDaoEntity> list, BmobException e) {
                        if (StringUtils.isEmpty(list)){
                            daoEntity.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e != null){
                                        if (e.getMessage().contains("unique")){
                                        }
                                    }
                                }
                            });
                        }else {
                            daoEntity.update(list.get(0).getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {

                                }
                            });
                        }
                    }
                });
            }else{
                DaoMaster master = GreenDaoHelper.getInstance().create(daoEntity.getDbName()).getMaster();
                if(master.newSession().getVideoDaoEntityDao().loadByRowId(daoEntity.getId())==null){
                    master.newSession()
                            .getVideoDaoEntityDao()
                            .insert(daoEntity);
                    e.onNext(true);
                }else {
                    master.newSession()
                            .getVideoDaoEntityDao()
                            .update(daoEntity);
                }
            }
        }).compose(RxUtils.applySchedulersWithLifeCycle(VideoDetailActivity.this))
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(@NonNull Boolean aBoolean) throws Exception {
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        }
                );
    }

    @Override
    public void finish() {
        saveVideoHistory(videoInfo);
        EventBus.getDefault().post("",EventBusTags.HISTORY_BACK_REFRESH);
        super.finish();
    }
}
