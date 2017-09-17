package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.utils.helper.MainFragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.HomeFragment;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwh on 2017/9/15 0015.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.nv_main_navigation)
    NavigationView mNvMainNavigation;
    @BindView(R.id.dl_main_drawer)
    DrawerLayout mDlMainDrawer;
    ActionBarDrawerToggle mToggle;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private HomeFragment mHomeFragment;

    private long firstTime = 0;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initFragment();
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(
                this, mDlMainDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDlMainDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
        mNvMainNavigation.setNavigationItemSelectedListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home:
                        mViewpager.setCurrentItem(0);
                        break;
                    case R.id.item_category:
                        mViewpager.setCurrentItem(1);
                        break;
                    case R.id.item_attention:
                        mViewpager.setCurrentItem(2);
                        break;
                    case R.id.item_mine:
                        mViewpager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.item_home);
    }

    private void initFragment() {
        mHomeFragment = HomeFragment.newInstance();
        List<BaseLazyLoadFragment> list = new ArrayList<>();
        list.add(mHomeFragment);
        list.add(HomeFragment.newInstance());
        list.add(HomeFragment.newInstance());
        list.add(HomeFragment.newInstance());
        mViewpager.setAdapter(MainFragmentAdapter.newInstance(getSupportFragmentManager(),list));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_manage) ;
        else if (item.getItemId() == R.id.nav_share) ;
        else if (item.getItemId() == R.id.nav_theme) ;
        mDlMainDrawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onBackPressed() {
        if (mDlMainDrawer.isDrawerOpen(GravityCompat.START)) {
            mDlMainDrawer.closeDrawer(GravityCompat.START);
        }
//        else if(fragments.get(position).isLoding()){
//            fragments.get(position).hideLoading();
//        }
        else {
            if (System.currentTimeMillis() - firstTime > 2000) {
                UiUtils.snackbarText("再按一次退出应用");
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        }
    }
}
