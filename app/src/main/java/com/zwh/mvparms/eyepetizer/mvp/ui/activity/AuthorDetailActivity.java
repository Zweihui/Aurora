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

import com.apt.TRouter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.annotation.aspect.CheckLogin;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.helper.AuthorFragmentAdapter;
import com.zwh.mvparms.eyepetizer.di.component.DaggerAuthorDetailComponent;
import com.zwh.mvparms.eyepetizer.di.module.AuthorDetailModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.AuthorDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyFollowedInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.AuthorDetailPresenter;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    AuthorTabsInfo info;
    private ShareInfo mShareInfo;

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
        if (savedInstanceState != null) {
            authorId = savedInstanceState.getInt(Constants.AUTHOR_ID);
        }
        mPresenter.getAuthorTabs(authorId);
        mPresenter.getShareInfo(authorId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.AUTHOR_ID, authorId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                switch (menuItem) {
                    case R.id.action_share:
                        if (mShareInfo != null){
                            gotoShare();
                        }
                        break;
                    case R.id.action_settings:
                        TRouter.go(Constants.SETTINGS);
                        break;
                    case R.id.action_unfollow:
                        follow(item.getTitle().toString());
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
    protected boolean isDisplayHomeAsUpEnabled() {
        return true;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void gotoShare() {
        if (mShareInfo == null)
            return;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, mShareInfo.getWechat_friends().getLink());
        intent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(intent, "分享到"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_author_detail, menu);
        menu.getItem(2).setTitle(MyFollowedInfo.getInstance().checkFollowed(authorId) ? "取消关注" : "关注");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(2).setTitle(MyFollowedInfo.getInstance().checkFollowed(authorId) ? "取消关注" : "关注");
        return super.onPrepareOptionsMenu(menu);
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
        for (AuthorTabsInfo.TabInfoBean.TabListBean bean : info.getTabInfo().getTabList()) {
            Category category = new Category();
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
        this.info = info;
    }

    @Override
    public void setShareInfo(ShareInfo info) {
        this.mShareInfo = info;
    }

    @Override
    public void setIndexInfo(AuthorIndexInfo info) {

    }

    @Override
    public void setAuthorAlbumInfo(List<AuthorAlbumInfo.Album> itemList, boolean isLoadMore) {

    }

    @Override
    public void setAuthorDynamicInfo(List<AuthorDynamicInfo.Dynamic> itemList, boolean isLoadMore) {

    }

    @Subscriber(tag = EventBusTags.AUTHOR_DETAIL_TAB)
    private void switchTabs(String tag) {
        if ("videos".equals(tag)) {
            mViewpager.setCurrentItem(1);
        }
        if ("dynamic".equals(tag)) {
            mViewpager.setCurrentItem(tlTabs.getTabCount() - 1);
        }
        if ("album".equals(tag)) {
            mViewpager.setCurrentItem(2);
        }
    }

    @CheckLogin
    private void follow(String title) {
        MyAttentionEntity attention = new MyAttentionEntity();
        attention.setId(authorId);
        attention.setTitle(info.getPgcInfo().getName());
        attention.setDescription(info.getPgcInfo().getDescription());
        attention.setUserId(BmobUser.getCurrentUser().getObjectId());
        attention.setIcon(info.getPgcInfo().getIcon());
        if ("取消关注".equals(title)) {
            invalidateOptionsMenu();
            BmobQuery<MyAttentionEntity> query = new BmobQuery<MyAttentionEntity>();
            query.addWhereEqualTo("id", authorId);
            query.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
            query.findObjects(new FindListener<MyAttentionEntity>() {
                @Override
                public void done(List<MyAttentionEntity> list, BmobException e) {
                    list.get(0).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                UiUtils.makeText(AuthorDetailActivity.this, e.getMessage());
                                return;
                            }
                            UiUtils.makeText(AuthorDetailActivity.this, "已取消关注");
                            for (int i = 0; i < MyFollowedInfo.getInstance().getList().size(); i++) {
                                if (MyFollowedInfo.getInstance().getList().get(i).getId() == attention.getId()) {
                                    MyFollowedInfo.getInstance().getList().remove(i);
                                }
                            }
                        }
                    });
                }
            });

        }
        if ("关注".equals(title)) {
            attention.setFollow(true);
            attention.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e != null) {
                        UiUtils.makeText(AuthorDetailActivity.this, e.getMessage());
                        return;
                    }
                    UiUtils.makeText(AuthorDetailActivity.this, "已关注");
                    MyFollowedInfo.getInstance().getList().add(attention);
                }
            });
        }
    }

}
