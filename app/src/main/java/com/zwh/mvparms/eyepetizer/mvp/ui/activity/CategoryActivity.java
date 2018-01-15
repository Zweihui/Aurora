package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.BaseApplication;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.helper.FragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;

import java.util.ArrayList;

import butterknife.BindView;

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
    public ArrayList<Category> list;

    @Extra(Constants.CATEGORY_DATA_POSITION)
    public int position;
    private long firstTime=0;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.CATEGORY_DATA,list);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return true;
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            list = savedInstanceState.getParcelableArrayList(Constants.CATEGORY_DATA);
            finish();
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mViewpager.setAdapter(FragmentAdapter.newInstance(getSupportFragmentManager(),list));
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CategoryActivity.this.position = position;
                ((BaseApplication)getApplication())
                        .getAppComponent()
                        .imageLoader().loadImage(CategoryActivity.this, ImageConfigImpl
                        .builder()
                        .url(list.get(position).getHeaderImage())
                        .imageView(mToolbarIvTarget)
                        .build());
                mCollapsingToolbar.setTitle(list.get(CategoryActivity.this.position).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
        ((BaseApplication)getApplication())
                .getAppComponent()
                .imageLoader().loadImage(CategoryActivity.this, ImageConfigImpl
                .builder()
                .url(list.get(0).getHeaderImage())
                .imageView(mToolbarIvTarget)
                .build());
        mViewpager.setOffscreenPageLimit(4);
        mViewpager.setCurrentItem(position);
        mCollapsingToolbar.setTitle(list.get(CategoryActivity.this.position).getName());
    }

}
