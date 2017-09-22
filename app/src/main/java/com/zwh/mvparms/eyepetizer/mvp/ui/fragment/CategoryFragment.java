package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerCategoryComponent;
import com.zwh.mvparms.eyepetizer.di.module.CategoryModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.CategoryContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.presenter.CategoryPresenter;

import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.id.list;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CategoryFragment extends BaseLazyLoadFragment<CategoryPresenter> implements CategoryContract.View,SwipeRefreshLayout.OnRefreshListener {


    private List<Category> mList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private CategoryAdapter adapter;


    private RxPermissions mRxPermissions;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        mRxPermissions = new RxPermissions(getActivity());
        DaggerCategoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .categoryModule(new CategoryModule(this))
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
    public void onRefresh() {
        mPresenter.requestCategories();
    }

    @Override
    protected void loadData() {
        adapter = new CategoryAdapter(R.layout.item_video_category,mList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                gotoDetail(view,position);
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setAdapter(adapter);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources()
                        .getDisplayMetrics()));
        mPresenter.requestCategories();
    }

    @SingleClick
    private void gotoDetail(View view,int position){
        TRouter.go(Constants.CATEGORY,new DataExtra(Constants.CATEGORY_DATA, mList).add(Constants.CATEGORY_DATA_POSITION,position).build());
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

    }


    @Override
    public void showLoading() {

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
    public void setData(List<Category> list) {
        mList.clear();
        if (!StringUtils.isEmpty(list)){
            mList.addAll(list);
        }
        adapter.setNewData(list);

    }

}
