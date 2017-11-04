package com.zwh.mvparms.eyepetizer.app.utils.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.VideoListFragment;

import java.util.List;

/**
 * Created by mac on 2017/8/20.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
//    private List<VideoListFragment> mFragments;//
    private List<Category> mCategories;

    public static FragmentAdapter newInstance(FragmentManager fm,List<Category> categories) {
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(fm);
//        mFragmentAdapter.mFragments = fragments;
        mFragmentAdapter.mCategories = categories;
        return mFragmentAdapter;
    }

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        return mFragments.get(position);
        return VideoListFragment.newInstance(mCategories.get(position));
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategories.get(position).getName();
    }
}
