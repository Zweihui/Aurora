package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerHotComponent;
import com.zwh.mvparms.eyepetizer.di.module.HotModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.HotContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.HotPresenter;

import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.category;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HotFragment extends BaseLazyLoadFragment<HotPresenter> implements HotContract.View, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private RxPermissions mRxPermissions;

    private List<VideoListInfo.Video> data = new ArrayList<>();
    private VideoAdapter adapter;

    public static HotFragment newInstance(Category category) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.TYPE, category.getDescription());
        HotFragment fragment = new HotFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private String type ="";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", type);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (String) getArguments().get(Constants.TYPE);
        if (savedInstanceState != null){
            type =  savedInstanceState.getString("type");
        }
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(getActivity());
        DaggerHotComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .hotModule(new HotModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_recyclerview, container, false);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        adapter = new VideoAdapter(R.layout.item_video_list,data);
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
    public void setData(List<VideoListInfo.Video> list) {
        data.clear();
        data.addAll(list);
        adapter.setNewData(data);
        adapter.notifyDataSetChanged();
    }

    @SingleClick
    private void gotoDetail(View view,int position){
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, data.get(position)).build(),view.findViewById(R.id.img_main));
    }

    @Override
    protected void loadData() {
        mSwipeRefresh.setRefreshing(true);
        mPresenter.getRankVideoList(type);
    }

    @Override
    public void setData(Object data) {

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

    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void onRefresh() {
        mPresenter.getRankVideoList(type);
    }
}
