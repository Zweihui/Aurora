package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;

import com.jess.arms.utils.UiUtils;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerVideoListActivityComponent;
import com.zwh.mvparms.eyepetizer.di.module.VideoListActivityModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoListActivityContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.VideoListActivityPresenter;

import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.AuthorVideosAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.HistoryAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Router(Constants.VIDEOLIST)
public class VideoListActivity extends BaseActivity<VideoListActivityPresenter> implements VideoListActivityContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Extra(Constants.COMMON_ID)
    public Integer id;
    @Extra(Constants.COMMON_TYPE)
    public String type;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private View footView;

    private AuthorVideosAdapter adapter;
    private List<VideoListInfo.Video> data = new ArrayList<>();

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerVideoListActivityComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .videoListActivityModule(new VideoListActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_video_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolBar();
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources()
                        .getDisplayMetrics()));
        adapter = new AuthorVideosAdapter(R.layout.item_video_detail_group_content,data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                gotoDetail(view,position);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
              fetchData(true);
            }
        },mRecyclerView);
        fetchData(false);
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
        if ("popular".equals(type)){
            setTitle("最受欢迎");
        }else {
            setTitle(type);
        }
    }

    @SingleClick
    private void gotoDetail(View view,int position){
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, adapter.getData().get(position)).build(),view.findViewById(R.id.iv_bg));
    }


    @Override
    public void showLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public void setVideos(List<VideoListInfo.Video> itemList, boolean isLoadMore) {
        if (itemList.size()<10){
            if (footView == null){
                footView = getLayoutInflater().inflate(R.layout.item_video_detail_foot, mRecyclerView, false);
            }
            if (adapter.getFooterLayoutCount()<=0){
                adapter.addFooterView(footView);
            }
            adapter.setEnableLoadMore(false);
        }
        if (isLoadMore){
            adapter.addData(itemList);
            adapter.loadMoreComplete();
        }else {
            adapter.setEnableLoadMore(true);
            if (adapter.getFooterLayoutCount()>0){
                adapter.removeFooterView(footView);
            }
            adapter.setNewData(itemList);
        }
    }

    @Override
    public void onRefresh() {
        fetchData(false);
    }

    private void fetchData(boolean isLoadMore){
        if ("popular".equals(type)){
            mPresenter.getPopularVideoList(id,isLoadMore?adapter.getData().size():0,isLoadMore);
        }else {
            mPresenter.getPlayListVideoList(id,isLoadMore?adapter.getData().size():0,isLoadMore);
        }
    }
}
