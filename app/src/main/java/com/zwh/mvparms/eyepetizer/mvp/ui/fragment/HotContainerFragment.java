package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.utils.helper.HotFragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.CustomViewPager;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class HotContainerFragment extends BaseLazyLoadFragment{

    CustomViewPager  mViewpager;

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    public static HotContainerFragment newInstance() {
        HotContainerFragment fragment = new HotContainerFragment();
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_container, container, false);
        mViewpager = (CustomViewPager ) view.findViewById(R.id.viewpager);
        mViewpager.setPagingEnabled(true);
        mViewpager.setOffscreenPageLimit(4);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onVisible() {
        super.onVisible();
        EventBus.getDefault().post(mViewpager, EventBusTags.HOT_FRAGMENT_SET_VIEWPAGER);
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    protected void loadData() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("weekly","周排行"));
        list.add(new Category("monthly","月排行"));
        list.add(new Category("historical","总排行"));
        mViewpager.setAdapter(HotFragmentAdapter.newInstance(getChildFragmentManager(),list));
        EventBus.getDefault().post(mViewpager, EventBusTags.HOT_FRAGMENT_SET_VIEWPAGER);
    }
}
