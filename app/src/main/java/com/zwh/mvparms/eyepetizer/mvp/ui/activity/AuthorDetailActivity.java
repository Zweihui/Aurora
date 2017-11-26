package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.apt.TRouter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.helper.AuthorFragmentAdapter;
import com.zwh.mvparms.eyepetizer.di.component.DaggerAuthorDetailComponent;
import com.zwh.mvparms.eyepetizer.di.module.AuthorDetailModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.AuthorDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.AuthorDetailPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Router(Constants.AUTHORDETAIL)
public class AuthorDetailActivity extends BaseActivity<AuthorDetailPresenter> implements AuthorDetailContract.View {

    @Extra(Constants.AUTHOR_ID)
    public int authorId;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tl_tabs)
    TabLayout tlTabs;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAuthorDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .authorDetailModule(new AuthorDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_author_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolBar();
        mPresenter.getAuthorTabs(authorId);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                switch (menuItem) {
                    case R.id.action_share:
                        break;
                    case R.id.action_settings:
                        TRouter.go(Constants.SETTINGS);
                        break;
                    case R.id.action_unfollow:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        setTitle("");
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_author_detail, menu);
        return true;
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.makeText(this, message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public void setVideosData(List<VideoListInfo.Video> videos, boolean isLoadMore) {

    }

    @Override
    public void setTabs(AuthorTabsInfo info) {
        List<Category> list = new ArrayList<>();
        for (AuthorTabsInfo.TabInfoBean.TabListBean bean:info.getTabInfo().getTabList()){
            Category category =new Category();
            category.setId(bean.getId());
            category.setName(bean.getName());
            category.setAuthorId(authorId);
            list.add(category);
        }
        info.getTabInfo().getTabList();
        mViewpager.setOffscreenPageLimit(4);
        mViewpager.setAdapter(AuthorFragmentAdapter.newInstance(getSupportFragmentManager(), list));
        tlTabs.setupWithViewPager(mViewpager);
        mViewpager.setCurrentItem(0);
        setTitle(info.getPgcInfo().getName());
    }

    @Override
    public void setIndexInfo(AuthorIndexInfo info) {

    }
}
