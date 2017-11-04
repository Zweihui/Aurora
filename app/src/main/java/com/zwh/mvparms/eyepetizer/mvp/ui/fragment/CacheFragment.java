package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DeviceUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DaoMaster;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfoDao;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.CacheActivity;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.HistoryActivity;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.CacheAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.service.DownLoadService;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import timber.log.Timber;

import static com.zwh.mvparms.eyepetizer.R.id.view;

/**
 * Created by mac on 2017/10/28.
 */

public class CacheFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private CacheAdapter adapter;
    private List<VideoDownLoadInfo> data = new ArrayList<>();
    private String type;
    private Gson mGson;
    private int currentPosition;
    private int changePosition;
    private View footView;
    private AppComponent mAppComponent;
    private RxPermissions mRxPermissions;

    public static CacheFragment newInstance(String type) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.TYPE, type);
        CacheFragment fragment = new CacheFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", type);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (String) getArguments().get(Constants.TYPE);
        if (savedInstanceState != null) {
            type = savedInstanceState.getString("type");
        }
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        mRxPermissions = new RxPermissions(getActivity());
        mAppComponent = appComponent;
        mGson = appComponent.gson();
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_recyclerview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setEnabled(false);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        adapter = new CacheAdapter(R.layout.item_cache, data, type);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                childClick(view, position);
            }
        });
        adapter.bindToRecyclerView(mRecyclerView);
        getDataFromDb();
    }

    @SingleClick
    private void childClick(View view, int position) {
        if (view.getId() == R.id.ll_progress) {
            changePosition = position;
            if (data.get(position).getDownLoading()){
                pauseVideo();
            }else{
                Intent intent = new Intent(getActivity(),DownLoadService.class);
                intent.putExtra(DownLoadService.VIDEOS_INFO,data.get(position));
                EventBus.getDefault().post(data.get(position),EventBusTags.CACHE_DOWNLOAD_BEGIN);
                getActivity().startService(intent);
            }
        }
        if (view.getId() == R.id.ll_detail) {
            gotoDetail(((ConstraintLayout)view.getParent()).findViewById(R.id.iv_bg),position);
        }
        if (view.getId() == R.id.iv_more) {
            initPopupWindow(view,position);
        }
        if (view.getId() == R.id.ctl_layout) {
            if (!type.contains("正在")) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunny_Videos/" + data.get(position).getId() + ".mp4";
                File file =  new File(path);
                if (!file.exists()){
//                    GreenDaoHelper.getInstance().create(data.get(position).getDbName())
//                            .getMaster().newSession()
//                            .getVideoDownLoadInfoDao().delete(data.get(position));
//                    adapter.notifyItemRemoved(position);
                    UiUtils.makeText(getActivity(),"缓存文件不存在");
                    return;
                }
                HashMap map = new DataExtra(Constants.VIDEO_URL, path).build();
                TRouter.go(Constants.FULL_VIDEO,map);
            } else {

            }
        }
    }

    private void gotoDetail(View view, int position) {
        VideoListInfo.Video video = new VideoListInfo.Video();
        video.setData(data.get(position).getVideo());
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, video).build(),view);
    }


    private void getDataFromDb() {
        Observable.create((ObservableOnSubscribe<List<VideoDownLoadInfo>>) e -> {
            VideoDownLoadInfo entity = new VideoDownLoadInfo();
            List<VideoDownLoadInfo> list = null;
            if (type.contains("正在")) {
                list = GreenDaoHelper.getInstance().create(entity.getDbName())
                        .getMaster().newSession()
                        .getVideoDownLoadInfoDao().queryBuilder()
                        .where(VideoDownLoadInfoDao.Properties.Finish.eq(false))
                        .orderDesc(VideoDownLoadInfoDao.Properties.CreatTime)
                        .list();
            } else {
                list = GreenDaoHelper.getInstance().create(entity.getDbName())
                        .getMaster().newSession()
                        .getVideoDownLoadInfoDao().queryBuilder()
                        .where(VideoDownLoadInfoDao.Properties.Finish.eq(true))
                        .orderDesc(VideoDownLoadInfoDao.Properties.CreatTime)
                        .list();
            }
            List<VideoDownLoadInfo> infolist = new ArrayList<VideoDownLoadInfo>();
            if (!StringUtils.isEmpty(list)) {
                for (VideoDownLoadInfo entity1 : list) {
                    entity1.setVideo(mGson.fromJson(entity1.getBody(), VideoListInfo.Video.VideoData.class));
                    infolist.add(entity1);
                }
            }
            e.onNext(infolist);
        }).compose(RxUtils.applySchedulersWithLifeCycle((CacheActivity) getActivity()))
                .subscribe(new Consumer<List<VideoDownLoadInfo>>() {
                               @Override
                               public void accept(@NonNull List<VideoDownLoadInfo> list) throws Exception {
                                   data.clear();
                                   data.addAll(list);
                                   adapter.notifyDataSetChanged();
                                   for (VideoDownLoadInfo info : data) {
                                       if (!info.getFinish()) {

                                       }
                                   }
                                   if (type.contains("正在")) {
                                       EventBus.getDefault().registerSticky(CacheFragment.this);
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

    public void initPopupWindow(View view,int position){
        PopupWindow popupWindow = new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.pop_cache, null));
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.getContentView().findViewById(R.id.tv_delete)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
                            @Override
                            public void onRequestPermissionSuccess() {
                                deleteVideoCache(data.get(position),position);
                            }

                            @Override
                            public void onRequestPermissionFailure() {
                                UiUtils.snackbarText("Request permissons failure");
                            }
                        }, mRxPermissions, mAppComponent.rxErrorHandler());
                        popupWindow.dismiss();
                    }
                });
        popupWindow.showAsDropDown(view, 0, -view.getHeight());
    }

    private void deleteVideoCache(VideoDownLoadInfo videoDownLoadInfo, int position) {
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            GreenDaoHelper.getInstance().create(videoDownLoadInfo.getDbName())
                    .getMaster().newSession()
                    .getVideoDownLoadInfoDao().delete(videoDownLoadInfo);
            File file =  new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sunny_Videos/" + videoDownLoadInfo.getId() + ".mp4");
            if (file.exists()){
                file.delete();
            }
            e.onNext(true);
        }).compose(RxUtils.applySchedulersWithLifeCycle((CacheActivity) getActivity()))
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(@NonNull Boolean success) throws Exception {
                                   data.remove(position);
                                   adapter.notifyItemRemoved(position);
                                   if (type.contains("正在")) {
                                       EventBus.getDefault().post("delete", EventBusTags.CACHE_DOWNLOAD_DELETE);
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

    private void showDownload(int position) {
        VideoDownLoadInfo item = data.get(position);
        item.setDownLoading(true);
        adapter.setDownPosition(position);
        SeekBar seekBar = (SeekBar) adapter.getViewByPosition(position, R.id.sb_progress);
        TextView size = (TextView) adapter.getViewByPosition(position, R.id.tv_pause);
        if (item.getContentLength() == null) {
            size.setText("连接中...");
        } else {
            size.setText(StringUtils.getPrintSize(item.getCurrentBytes() == null ? 0 : item.getCurrentBytes(), false) + "/" + StringUtils.getPrintSize(item.getContentLength() == null ? 0 : item.getContentLength(), true));
        }
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setProgress(data.get(position).getPercent());
    }

    private void hideDownload(int position) {
        VideoDownLoadInfo item = data.get(position);
        item.setDownLoading(false);
        SeekBar seekBar = (SeekBar) adapter.getViewByPosition(position, R.id.sb_progress);
        TextView size = (TextView) adapter.getViewByPosition(position, R.id.tv_pause);
        item.setLineUp(false);
        size.setText("已暂停");
        seekBar.setVisibility(View.GONE);
    }

    private void pauseVideo() {
        Intent intent = new Intent(getActivity(), DownLoadService.class);
        intent.putExtra(DownLoadService.PAUSE_DOWNLOAD, true);
        getActivity().startService(intent);
    }

    @Subscriber(tag = EventBusTags.CACHE_DOWNLOAD_FINISH)
    private void downloadFinish(String tag) {
        getDataFromDb();
    }

    @Subscriber(tag = EventBusTags.CACHE_DOWNLOAD_PROGRESS)
    private void downloadProgress(ProgressInfo progressInfo) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(progressInfo.getId())) {
                currentPosition = i;
                changePosition = i;
                showDownload(i);
            }
        }
        VideoDownLoadInfo item = data.get(currentPosition);
        item.setDownLoading(true);
        item.setCurrentBytes(progressInfo.getCurrentbytes());
        item.setContentLength(progressInfo.getContentLength());
    }

    @Subscriber(tag = EventBusTags.CACHE_DOWNLOAD_CACNCEL)
    private void downloadCancel(Long id) {
//        if (id.equals(-1L)){
        int position = -1;
        for (int i=0;i<data.size();i++){
            if (data.get(i).getId().equals(id)){
                position = i;
            }
        }
        if (position >=0){
            if (data.get(position).getDownLoading()) {
                hideDownload(position);
            } else {
                showDownload(position);
                Intent intent = new Intent(getActivity(), DownLoadService.class);
                intent.putExtra(DownLoadService.VIDEOS_INFO, data.get(position));
                intent.putExtra(DownLoadService.VIDEOS_INSERT, false);
                getActivity().startService(intent);
            }
        }

//        }else {
//            for (int i=0;i<data.size();i++){
//                if (data.get(i).getId().equals(id)){
//                    currentPosition = i;
//                    changePosition = i;
//                    data.get(i).setLineUp(false);
//                }
//            }
//            showDownload();
//        }

    }

    @Override
    public void setData(Object data) {

    }

    //排队
    @Subscriber(tag = EventBusTags.CACHE_DOWNLOAD_BEGIN)
    private void downloadBegin(VideoDownLoadInfo info) {
        Timber.e("-------------getSticky");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(info.getId())) {
                currentPosition = i;
                changePosition = i;
                data.get(i).setLineUp(true);
                adapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRxPermissions = null;
    }
}
