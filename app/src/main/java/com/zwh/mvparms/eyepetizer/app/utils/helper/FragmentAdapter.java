package com.zwh.mvparms.eyepetizer.app.utils.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.VideoListFragment;

import java.util.List;

/**
 * Created by mac on 2017/8/20.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<VideoListFragment> mFragments;
    private List<String> mTitles;

    public static FragmentAdapter newInstance(FragmentManager fm, List<VideoListFragment> fragments, List<String> titles) {
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(fm);
        mFragmentAdapter.mFragments = fragments;
        mFragmentAdapter.mTitles = titles;
        return mFragmentAdapter;
    }

    public FragmentAdapter(FragmentManager fm) {
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

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
