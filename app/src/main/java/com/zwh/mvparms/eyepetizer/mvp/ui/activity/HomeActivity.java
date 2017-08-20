package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.helper.FragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.VideoListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


@Router(Constants.HOME)
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.main_content)
    CoordinatorLayout mMainContent;
    @BindView(R.id.nv_main_navigation)
    NavigationView mNvMainNavigation;
    @BindView(R.id.dl_main_drawer)
    DrawerLayout mDlMainDrawer;
//    @BindView(R.id.toolbar_iv_outgoing)
//    ImageView mToolbarIvOutgoing;
    @BindView(R.id.toolbar_iv_target)
    ImageView mToolbarIvTarget;
    @BindView(R.id.tl_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;


    ActionBarDrawerToggle mToggle;

    @Override
    public void onBackPressed() {
        if (mDlMainDrawer.isDrawerOpen(GravityCompat.START)) {
            mDlMainDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_manage) ;
        else if (item.getItemId() == R.id.nav_share) ;
        else if (item.getItemId() == R.id.nav_theme) ;
        mDlMainDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(
                this, mDlMainDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDlMainDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
        mNvMainNavigation.setNavigationItemSelectedListener(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        List<String> mTabs = new ArrayList<>();
        mTabs.add("热门");
        mTabs.add("热门");
        mTabs.add("热门");
        mTabs.add("热门");
        mTabs.add("热门");
        mTabs.add("热门");
        mTabs.add("热门");
        mTabs.add("热门");
        mTabs.add("热门");
        mTabs.add("热门");
        List<VideoListFragment> listFragments = new ArrayList<>();
        for(String type :mTabs){
            listFragments.add(VideoListFragment.newInstance(type));
        }
        mViewpager.setAdapter(FragmentAdapter.newInstance(getSupportFragmentManager(),listFragments,mTabs));
//        Observable.fromIterable(mTabs)
//                .map(VideoListFragment::newInstance)
//                .toList()
//                .map(fragments -> FragmentAdapter.newInstance(getSupportFragmentManager(), fragments, mTabs))
//                .subscribe(mFragmentAdapter -> mViewpager.setAdapter(mFragmentAdapter));
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
    }
}
