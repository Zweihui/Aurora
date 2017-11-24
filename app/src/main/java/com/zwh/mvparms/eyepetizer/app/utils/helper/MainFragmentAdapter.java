package com.zwh.mvparms.eyepetizer.app.utils.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jess.arms.base.BaseLazyLoadFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.AttentionContainerFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.CategoryFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.HomeFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.HotContainerFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.MineFragment;

import java.util.List;

/**
 * Created by mac on 2017/9/16.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<String> mFragments;

    public static MainFragmentAdapter newInstance(FragmentManager fm, List<String> fragments) {
        MainFragmentAdapter mFragmentAdapter = new MainFragmentAdapter(fm);
        mFragmentAdapter.mFragments = fragments;
        return mFragmentAdapter;
    }

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        String type = mFragments.get(position);
        switch (type){
            case "home":
                return HomeFragment.newInstance();
            case "category":
                return CategoryFragment.newInstance();
            case "attention":
                return AttentionContainerFragment.newInstance();
            case "mine":
                return MineFragment.newInstance();
            case "hot":
                return HotContainerFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
