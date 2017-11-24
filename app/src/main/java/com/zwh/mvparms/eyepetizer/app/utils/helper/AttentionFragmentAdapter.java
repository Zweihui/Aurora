package com.zwh.mvparms.eyepetizer.app.utils.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.AttentionFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.HotFragment;

import java.util.List;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class AttentionFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Category> mCategories;

    public static AttentionFragmentAdapter newInstance(FragmentManager fm, List<Category> categories) {
        AttentionFragmentAdapter mFragmentAdapter = new AttentionFragmentAdapter(fm);
        mFragmentAdapter.mCategories = categories;
        return mFragmentAdapter;
    }

    public AttentionFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return AttentionFragment.newInstance(mCategories.get(position));
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
