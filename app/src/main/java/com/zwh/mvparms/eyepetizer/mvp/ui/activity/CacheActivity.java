package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.mvp.IView;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.app.utils.helper.CacheFragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfoDao;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.CacheFragment;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

@Router(Constants.CACHE)
public class CacheActivity extends BaseActivity implements IView{

    public final static String FROM_NOTIFICATION = "from_notification";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tl_tabs)
    TabLayout mTlTabs;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    private boolean isFromNotification = false;

    private AppComponent mAppComponent;
    private List<CacheFragment> fragments = new ArrayList<>();
    private int size = 0;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_cache;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        isFromNotification = getIntent().getBooleanExtra(FROM_NOTIFICATION,false);
        initToolBar();
        VideoDownLoadInfo entity = new VideoDownLoadInfo();
        List<VideoDownLoadInfo> infos = GreenDaoHelper.getInstance().create(entity.getDbName())
                .getMaster().newSession()
                .getVideoDownLoadInfoDao().queryBuilder()
                .where(VideoDownLoadInfoDao.Properties.Finish.eq(false ))
                .list();
        List<String> list = new ArrayList<>();
        list.add("已缓存");
        list.add("正在缓存"+"("+(infos==null?0:infos.size())+")");
        Observable.fromIterable(list)
                .map(new Function<String, CacheFragment>() {
                    @Override
                    public CacheFragment apply(@NonNull String category) throws Exception {
                        CacheFragment fragment = CacheFragment.newInstance(category);
                        fragments.add(fragment);
                        return fragment;
                    }
                })
                .toList()
                .map(fragments -> CacheFragmentAdapter.newInstance(getSupportFragmentManager(), fragments, list))
                .subscribe(mFragmentAdapter -> mViewpager.setAdapter(mFragmentAdapter));
        mTlTabs.setupWithViewPager(mViewpager);
        if (isFromNotification){
            mViewpager.setCurrentItem(1);
        }else {
            mViewpager.setCurrentItem(0);
        }

    }

    @Subscriber(tag = EventBusTags.CACHE_DOWNLOAD_FINISH)
    private void downloadFinish(String tag) {
        VideoDownLoadInfo entity = new VideoDownLoadInfo();
        List<VideoDownLoadInfo> infos = GreenDaoHelper.getInstance().create(entity.getDbName())
                .getMaster().newSession()
                .getVideoDownLoadInfoDao().queryBuilder()
                .where(VideoDownLoadInfoDao.Properties.Finish.eq(false ))
                .list();
        mTlTabs.getTabAt(1).setText("正在缓存"+"("+(infos==null?0:infos.size())+")");
        size = infos.size();
    }
    @Subscriber(tag = EventBusTags.CACHE_DOWNLOAD_DELETE)
    private void downloadDelete(String tag) {
        size = size - 1;
        mTlTabs.getTabAt(1).setText("正在缓存"+"("+size+")");

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(VideoDownLoadInfo.class,EventBusTags.CACHE_DOWNLOAD_BEGIN);
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return true;
    }
}
