package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.zwh.annotation.apt.Router;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerMyAttentionComponent;
import com.zwh.mvparms.eyepetizer.di.module.AttentionModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.AttentionContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AttentionInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.presenter.AttentionPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.AurhorListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;

@Router(Constants.MYATTENTION)
public class MyAttentionActivity extends BaseActivity<AttentionPresenter> implements AttentionContract.View, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private View footView;
    private BaseQuickAdapter adapter;
    private List<MyAttentionEntity> authors = new ArrayList<>();

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyAttentionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .attentionModule(new AttentionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_attention;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolBar();
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources()
                        .getDisplayMetrics()));
        adapter = new AurhorListAdapter(R.layout.item_author_list, authors);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                gotoAuthorDetail(view, position);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        mPresenter.getMyAttentionList(BmobUser.getCurrentUser().getObjectId());
    }

    @SingleClick
    private void gotoAuthorDetail(View view, int position) {
        TRouter.go(Constants.AUTHORDETAIL, new DataExtra(Constants.AUTHOR_ID, authors.get(position).getId()).build());
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

    @Override
    public void onRefresh() {
        mPresenter.getMyAttentionList(BmobUser.getCurrentUser().getObjectId());
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
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public void setData(List<AttentionInfo.ItemListBeanX> itemList, boolean isLoadMore) {

    }

    @Override
    public void setAuthorList(List<MyAttentionEntity> list) {
        authors.clear();
        authors.addAll(list);
        adapter.setNewData(list);
        mSwipeRefresh.setRefreshing(false);
        if (footView == null) {
            footView = getLayoutInflater().inflate(R.layout.item_video_detail_foot, mRecyclerView, false);
            adapter.addFooterView(footView);
        }
    }
}
