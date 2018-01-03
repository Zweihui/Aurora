package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apt.TRouter;
import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.helper.AttentionFragmentAdapter;
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

    }

    @Override
    public void onVisible() {
        super.onVisible();
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    protected void loadData() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("all","热门关注"));
        list.add(new Category("mine","我的关注"));
        mViewpager.setAdapter(AttentionFragmentAdapter.newInstance(getChildFragmentManager(),list));
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1&& BmobUser.getCurrentUser()==null){
                    TRouter.go(Constants.LOGIN);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        EventBus.getDefault().post(mViewpager, EventBusTags.HOT_FRAGMENT_SET_VIEWPAGER);
    }
}
