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

import static com.zwh.mvparms.eyepetizer.R.id.toolbar;


@Router(Constants.CATEGORY)
public class CategoryActivity extends BaseActivity{

    @BindView(toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.main_content)
    CoordinatorLayout mMainContent;
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

    @Extra(Constants.CATEGORY_DATA)
    public List<Category> list;

    @Extra(Constants.CATEGORY_DATA_POSITION)
    public int position;
    private long firstTime=0;
    private List<VideoListFragment> fragments = new ArrayList<>();

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                CategoryActivity.this.position = position;
                ((BaseApplication)getApplication())
                        .getAppComponent()
                        .imageLoader().loadImage(CategoryActivity.this, GlideImageConfig
                        .builder()
                        .url(list.get(position).getHeaderImage())
                        .imageView(mToolbarIvTarget)
                        .build());
                setTitle(list.get(CategoryActivity.this.position).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
        ((BaseApplication)getApplication())
                .getAppComponent()
                .imageLoader().loadImage(CategoryActivity.this, GlideImageConfig
                .builder()
                .url(list.get(0).getHeaderImage())
                .imageView(mToolbarIvTarget)
                .build());
        mViewpager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        if(fragments.get(position).isLoding()){
            fragments.get(position).hideLoading();
        }else {
            super.onBackPressed();
        }
    }
}
