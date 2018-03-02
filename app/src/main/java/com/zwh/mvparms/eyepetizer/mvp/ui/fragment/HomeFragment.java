package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.SharedPreferencesUtils;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerHomeComponent;
import com.zwh.mvparms.eyepetizer.di.module.VideoModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.VideoPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.DefaultVideoAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.VideoAdapter;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import static com.zwh.mvparms.eyepetizer.app.constants.Constants.SP_LAST_START_ID;

/**
 * Created by mac on 2017/9/16.
 */

public class  HomeFragment extends BaseLazyLoadFragment<VideoPresenter> implements VideoContract.View,SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefresh;
    RecyclerView mRecyclerView;

    private BaseQuickAdapter adapter;
    private List<VideoListInfo.Video> data = new ArrayList<>();

    private RxPermissions mRxPermissions;
    private boolean isFirstLoad = true;

    private int page = 2;
    private int lastStartId = -1;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    @Override
    public void onRefresh() {
        mPresenter.getIndexVideoList(true,page);
    }

    @Override
    public void showLoading() {
        if (!mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
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

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(getActivity());
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .videoModule(new VideoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.common_recyclerview, container, false);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        adapter = new VideoAdapter(R.layout.item_video_list,data);
        adapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getIndexVideoList(false,page);
            }
        },500), mRecyclerView);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources()
                        .getDisplayMetrics()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 20);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                gotoDetail(view,position);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setData(Object data) {

    }

    @SingleClick
    private void gotoDetail(View view,int position){
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, adapter.getData().get(position)).build(),view.findViewById(R.id.img_main));
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void setData(List<VideoListInfo.Video> list, Boolean isPullRefresh) {
        if (isPullRefresh){
            if (isFirstLoad){
                isFirstLoad = false;
                data.clear();
                data.addAll(list);
                adapter = new DefaultVideoAdapter(R.layout.item_video_default_list,data);
                adapter.setNewData(data);
                mRecyclerView.setAdapter(adapter);
                mSwipeRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new VideoAdapter(R.layout.item_video_list,data);
                        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                            @Override
                            public void onLoadMoreRequested() {
                                mRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPresenter.getIndexVideoList(false,page);
                                    }
                                },500);
                            }
                        }, mRecyclerView);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                gotoDetail(view,position);
                            }
                        });
                        mRecyclerView.setAdapter(adapter);
                    }
                },1000);
                return;
            }else {
                data.clear();
                data.addAll(list);
                adapter.setNewData(list);
                mRecyclerView.scrollToPosition(0);
                adapter.setEnableLoadMore(true);
                UiUtils.makeText(getActivity(),"更新了"+list.size()+"条内容");
            }
            lastStartId = list.get(0).getData().getId();
            SharedPreferencesUtils.setParam(getActivity(),SP_LAST_START_ID,lastStartId);
        }else {
            adapter.addData(list);
            adapter.loadMoreComplete();
            page = page + 2;
        }
    }

    @Override
    protected void loadData() {
        lastStartId = (int) SharedPreferencesUtils.getParam(getActivity(),SP_LAST_START_ID,-1);
        mSwipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getIndexVideoList(true,page);
                mSwipeRefresh.setRefreshing(true);
            }
        },300);
    }
    @Subscriber(tag = EventBusTags.REFRESH_HOME_DATA)
    public void refrshData(String s){
        if (mSwipeRefresh.isRefreshing()){
            return;
        }
        mSwipeRefresh.setRefreshing(true);
        mPresenter.getIndexVideoList(true,page);
    }
    public int getFirstQuryId() {
        if (StringUtils.isEmpty(data)){
            return 1;
        }else {
            return data.get(0).getData().getId();
        }
    }
}
