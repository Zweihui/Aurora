package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.zwh.annotation.apt.Router;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerHistoryComponent;
import com.zwh.mvparms.eyepetizer.di.module.HistoryModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.HistoryContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDaoEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.HistoryPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.HistoryAdapter;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Router(Constants.HISTORY)
public class HistoryActivity extends BaseActivity<HistoryPresenter> implements HistoryContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private View footView;

    public final static int REQUEST_DETAIL_BACK = 200;

    private HistoryAdapter adapter;
    private List<VideoDaoEntity> data = new ArrayList<>();

    private AppComponent mAppComponent;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
        DaggerHistoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .historyModule(new HistoryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_history; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolBar();
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources()
                        .getDisplayMetrics()));
        adapter = new HistoryAdapter(R.layout.item_history,data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                gotoDetail(view,position);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                initPopupWindow(view,position);
            }
        });
        footView = getLayoutInflater().inflate(R.layout.item_video_detail_foot, mRecyclerView, false);
        adapter.addFooterView(footView);
        footView.setVisibility(View.GONE);
        refreshData("");
    }

    @Subscriber(tag = EventBusTags.HISTORY_BACK_REFRESH)
    private void refreshData(String tag) {
        mPresenter.getListFromDb(0,false);
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
    }

    @SingleClick
    private void gotoDetail(View view,int position){
        VideoListInfo.Video videoInfo = new VideoListInfo.Video();
        videoInfo.setData(data.get(position).getVideo());
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, videoInfo).build(),view.findViewById(R.id.iv_bg));
    }

    @Override
    public void setData(List<VideoDaoEntity> list, Boolean isLoadMore) {
        if (list.size()<10){
            adapter.setEnableLoadMore(false);
            footView.setVisibility(View.VISIBLE);
        }else {
            footView.setVisibility(View.GONE);
            adapter.setEnableLoadMore(true);
            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    mPresenter.getListFromDb(data.size()-1,true);
                }
            },mRecyclerView);
        }
        if (isLoadMore){
            adapter.addData(list);
            adapter.loadMoreComplete();
        }else {
            data.clear();
            data.addAll(list);
            adapter.setNewData(data);
        }
    }


    public void initPopupWindow(View view,int position){
        PopupWindow popupWindow = new PopupWindow(HistoryActivity.this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(LayoutInflater.from(HistoryActivity.this).inflate(R.layout.pop_history, null));
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.getContentView().findViewById(R.id.tv_delete)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteVideoHistory(data.get(position),position);
                        popupWindow.dismiss();
                    }
                });
        popupWindow.getContentView().findViewById(R.id.tv_share)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoShare();
                        popupWindow.dismiss();
                    }
                });
            popupWindow.showAsDropDown(view, 0, -view.getHeight());
    }

    private void gotoShare() {
    }

    private void deleteVideoHistory(VideoDaoEntity daoEntity,int position) {
        mPresenter.deleteFromDb(daoEntity,position);
    }

    @Override
    public void deleteData(int position) {
        data.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void showLoading() {
        if (!mSwipeRefresh.isRefreshing())
            mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefresh.isRefreshing())
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
    public void onRefresh() {
        refreshData("");
    }
}
