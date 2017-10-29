package com.zwh.mvparms.eyepetizer.app.utils.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.CacheFragment;

import java.util.List;

/**
 * Created by mac on 2017/10/28.
 */

public class CacheFragmentAdapter extends FragmentStatePagerAdapter {

    private List<CacheFragment> mFragments;
    private List<String> mCategories;

    public static CacheFragmentAdapter newInstance(FragmentManager fm, List<CacheFragment> fragments, List<String> categories) {
        CacheFragmentAdapter mFragmentAdapter = new CacheFragmentAdapter(fm);
        mFragmentAdapter.mFragments = fragments;
        mFragmentAdapter.mCategories = categories;
        return mFragmentAdapter;
    }

    public CacheFragmentAdapter(FragmentManager fm) {
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
        return mCategories.get(position);
    }
}
