package com.zwh.mvparms.eyepetizer.app.utils.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jess.arms.base.BaseLazyLoadFragment;

import java.util.List;

/**
 * Created by mac on 2017/9/16.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<BaseLazyLoadFragment> mFragments;

    public static MainFragmentAdapter newInstance(FragmentManager fm, List<BaseLazyLoadFragment> fragments) {
        MainFragmentAdapter mFragmentAdapter = new MainFragmentAdapter(fm);
        mFragmentAdapter.mFragments = fragments;
        return mFragmentAdapter;
    }

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
