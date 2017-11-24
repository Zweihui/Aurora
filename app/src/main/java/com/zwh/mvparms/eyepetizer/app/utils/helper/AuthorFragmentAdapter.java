package com.zwh.mvparms.eyepetizer.app.utils.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.AuthorAlbumFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.AuthorIndexFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.AuthorTrendFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.AuthorVideoFragment;

import java.util.List;

/**
 * Created by Administrator on 2017\11\23 0023.
 */

public class AuthorFragmentAdapter extends FragmentPagerAdapter{

    private List<Category> mCategories;

    public AuthorFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public static AuthorFragmentAdapter newInstance(FragmentManager fm,List<Category> categories) {
        AuthorFragmentAdapter mFragmentAdapter = new AuthorFragmentAdapter(fm);
        mFragmentAdapter.mCategories = categories;
        return mFragmentAdapter;
    }

    @Override
    public Fragment getItem(int position) {
        int id = mCategories.get(position).getId();
        switch (id){
            case 0:
                return AuthorIndexFragment.newInstance(mCategories.get(position).getAuthorId());
            case 1:
                return AuthorVideoFragment.newInstance(mCategories.get(position).getAuthorId());
            case 2:
                return AuthorAlbumFragment.newInstance(mCategories.get(position).getAuthorId());
            case 3:
                return AuthorTrendFragment.newInstance(mCategories.get(position).getAuthorId());
            default:
                return null;
        }
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
