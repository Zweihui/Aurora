package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.utils.helper.AttentionFragmentAdapter;
import com.zwh.mvparms.eyepetizer.app.utils.helper.HotFragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.CustomViewPager;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class AttentionContainerFragment extends BaseLazyLoadFragment {

    CustomViewPager  mViewpager;

    public static AttentionContainerFragment newInstance() {
        AttentionContainerFragment fragment = new AttentionContainerFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_container, container, false);
        mViewpager = (CustomViewPager ) view.findViewById(R.id.viewpager);
        if (BmobUser.getCurrentUser()!=null){
            mViewpager.setPagingEnabled(true);
        }else {
            mViewpager.setPagingEnabled(false);
        }
        mViewpager.setOffscreenPageLimit(4);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        List<Category> list = new ArrayList<>();
        list.add(new Category("all","热门关注"));
        list.add(new Category("mine","我的关注"));
        mViewpager.setAdapter(AttentionFragmentAdapter.newInstance(getChildFragmentManager(),list));
        EventBus.getDefault().post(mViewpager, EventBusTags.HOT_FRAGMENT_SET_VIEWPAGER);
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    protected void loadData() {

    }
}
