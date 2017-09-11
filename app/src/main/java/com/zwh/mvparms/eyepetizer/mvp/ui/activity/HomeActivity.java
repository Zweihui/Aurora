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

import com.jess.arms.base.BaseApplication;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.helper.FragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.VideoListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


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
    @Extra(Constants.SPLASH_DATA)
    public List<Category> list;

    private int position = 0;
    private long firstTime=0;
    private List<VideoListFragment> fragments = new ArrayList<>();

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
        Observable.fromIterable(list)
                .map(new Function<Category, VideoListFragment>() {
                    @Override
                    public VideoListFragment apply(@NonNull Category category) throws Exception {
                        VideoListFragment fragment = VideoListFragment.newInstance(category);
                        fragments.add(fragment);
                        return fragment;
                    }
                })
                .toList()
                .map(fragments -> FragmentAdapter.newInstance(getSupportFragmentManager(), fragments, list))
                .subscribe(mFragmentAdapter -> mViewpager.setAdapter(mFragmentAdapter));
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                HomeActivity.this.position = position;
                ((BaseApplication)getApplication())
                        .getAppComponent()
                        .imageLoader().loadImage(HomeActivity.this, GlideImageConfig
                        .builder()
                        .url(list.get(position).getHeaderImage())
                        .imageView(mToolbarIvTarget)
                        .build());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
        ((BaseApplication)getApplication())
                .getAppComponent()
                .imageLoader().loadImage(HomeActivity.this, GlideImageConfig
                .builder()
                .url(list.get(0).getHeaderImage())
                .imageView(mToolbarIvTarget)
                .build());
    }

    @Override
    public void onBackPressed() {
        if (mDlMainDrawer.isDrawerOpen(GravityCompat.START)) {
            mDlMainDrawer.closeDrawer(GravityCompat.START);
        } else if(fragments.get(position).isLoding()){
            fragments.get(position).hideLoading();
        }else {
            if (System.currentTimeMillis()-firstTime>2000){
                UiUtils.snackbarText("再按一次退出应用");
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
        }
    }

}
