package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.annotation.apt.AutoRestore;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerVideoComponent;
import com.zwh.mvparms.eyepetizer.di.module.VideoModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.VideoPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.DefaultVideoAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2017/8/20.
 */

public class VideoListFragment extends BaseLazyLoadFragment<VideoPresenter> implements VideoContract.View,SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefresh;
    RecyclerView mRecyclerView;

    private BaseQuickAdapter adapter;
    private List<VideoListInfo.Video> data = new ArrayList<>();
    private RxPermissions mRxPermissions;
    private boolean isFirstLoad = true;
    @AutoRestore
    public String type ="";

    public static VideoListFragment newInstance(Category category) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.TYPE, category.getId()+"");
        VideoListFragment fragment = new VideoListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (String) getArguments().get(Constants.TYPE);
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(getActivity());
        DaggerVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .videoModule(new VideoModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void loadData() {
        mPresenter.getVideoList(type,getQuryId(),0,true);
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void showLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showMessage(String message) {
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

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
                mPresenter.getVideoList(type,getQuryId(),getStartCount(),false);
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
    public int getStartCount() {
        if (StringUtils.isEmpty(data)){
            return 0;
        }else {
            return data.size();
        }
    }
    public int getQuryId() {
        if (StringUtils.isEmpty(data)){
            return 1;
        }else {
            return data.get(data.size()-1).getData().getId();
        }
    }

    @Override
    public void onRefresh() {
        if (mPresenter == null){
        }
        mPresenter.getVideoList(type,getQuryId(),0,true);
    }

    public boolean isLoding(){
        return mSwipeRefresh.isRefreshing();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @SingleClick
    private void gotoDetail(View view,int position){
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, data.get(position)).build(),view.findViewById(R.id.img_main));
    }

    @Override
    public void setData(List<VideoListInfo.Video> list,Boolean isPullRefresh) {
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
                                        mPresenter.getVideoList(type,getQuryId(),getStartCount(),false);
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
            }
            data.clear();
            data.addAll(list);
            adapter.setNewData(data);
            adapter.setEnableLoadMore(true);
        }else {
            adapter.addData(list);
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRxPermissions = null;
    }
}
