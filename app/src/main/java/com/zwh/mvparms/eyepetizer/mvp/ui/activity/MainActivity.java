package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.apt.TRouter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.ui.MatisseActivity;
import com.zwh.annotation.apt.Router;
import com.zwh.annotation.aspect.CheckLogin;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.helper.MainFragmentAdapter;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyFollowedInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.BottomNavigationViewHelper;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.CircleImageView;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.CustomViewPager;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.MaterialSearchView;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.transition.SearchTransitioner;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.zwh.mvparms.eyepetizer.R.id.toolbar;
import static com.zwh.mvparms.eyepetizer.mvp.ui.fragment.MineFragment.REQUEST_CODE_CHOOSE;

/**
 * Created by zwh on 2017/9/15 0015.
 */
@Router(Constants.MAIN)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.tl_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.nv_main_navigation)
    NavigationView mNvMainNavigation;
    @BindView(R.id.dl_main_drawer)
    DrawerLayout mDlMainDrawer;
    ActionBarDrawerToggle mToggle;
    @BindView(R.id.viewpager)
    CustomViewPager mViewpager;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
//    private HomeFragment mHomeFragment;
//    private CategoryFragment mCategoryFragment;
//    private MineFragment mMineFragment;
//    private HotContainerFragment mHotContainerFragment;
//    private AttentionContainerFragment mAttentionFragment;
//    private HotFragment mHotFragment;

    private SearchTransitioner searchTransitioner;

    private long firstTime = 0;
    private int usableHeightPrevious;
    boolean isSoftKeyBoardShow = false;
    boolean isShowTransition = false;
    AppComponent appComponent;
    private RxPermissions rxPermissions;
    private RxErrorHandler mErrorHandler;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        rxPermissions = new RxPermissions(this);
        mErrorHandler = appComponent.rxErrorHandler();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bmob.initialize(this, Constants.BMOB_APP_ID);
        initFragment();
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(
                this, mDlMainDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDlMainDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
        mNvMainNavigation.setNavigationItemSelectedListener(this);
        mNvMainNavigation.getHeaderView(0).findViewById(R.id.im_face).setOnClickListener(this);
        mNvMainNavigation.getHeaderView(0).findViewById(R.id.tv_name).setOnClickListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home:
                        mViewpager.setCurrentItem(0);
                        setTitle("首页");
                        mTabLayout.setVisibility(View.GONE);
                        break;
                    case R.id.item_category:
                        mViewpager.setCurrentItem(1);
                        setTitle("分类");
                        mTabLayout.setVisibility(View.GONE);
                        break;
                    case R.id.item_hot:
                        mViewpager.setCurrentItem(2);
                        setTitle("热门");
                        mTabLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_attention:
                        mViewpager.setCurrentItem(3);
                        setTitle("关注");
                        mTabLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_mine:
                        mViewpager.setCurrentItem(4);
                        setTitle("我的");
                        mTabLayout.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.item_home && ((Fragment)mViewpager.getAdapter().instantiateItem(mViewpager,0)).isVisible()) {
                    EventBus.getDefault().post("showLoading", EventBusTags.REFRESH_HOME_DATA);
                }
            }
        });
        bottomNavigation.setSelectedItemId(R.id.item_home);
        mDlMainDrawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        initToolBar();
        searchTransitioner = new SearchTransitioner(this, mViewpager, searchView);
        mDlMainDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = BmobUser.getCurrentUser(User.class);
                if (user != null){
                    EventBus.getDefault().post(user, EventBusTags.SET_USER_INFO);
                }
            }
        },800);
        setTitle("首页");
    }

    private void initToolBar() {
        searchView.setToolbar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                switch (menuItem) {
                    case R.id.action_search:
                        searchView.showView();
                        searchView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gotoSearch();
                            }
                        }, 300);
                        break;
                    case R.id.action_settings:
                        TRouter.go(Constants.SETTINGS);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void gotoSearch() {
        searchTransitioner.transitionToSearch();
        isShowTransition = true;
    }

    private void initFragment() {
//        mHomeFragment = HomeFragment.newInstance();
//        mCategoryFragment = CategoryFragment.newInstance();
//        mMineFragment = MineFragment.newInstance();
//        mHotContainerFragment = HotContainerFragment.newInstance();
//        mAttentionFragment = AttentionContainerFragment.newInstance();
//        mHotFragment = HotFragment.newInstance(new Category("weekly","周排行"));
        List<String> list = new ArrayList<>();
        list.add("home");
        list.add("category");
        list.add("hot");
        list.add("attention");
        list.add("mine");
        mViewpager.setOffscreenPageLimit(4);
        mViewpager.setPagingEnabled(false);
        mViewpager.setAdapter(MainFragmentAdapter.newInstance(getSupportFragmentManager(), list));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_face:
            case R.id.tv_name:
                gotoLogin();
                break;
        }
    }

    @CheckLogin
    private void gotoLogin(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDlMainDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchTransitioner.onActivityResumed();
            }
        }, 250);
        mDlMainDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                isShowTransition = false;
                if (searchView.isShowing()) {
                    searchView.showView();
                }
            }
        }, 500);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_login) {
        }
        if (item.getItemId() == R.id.nav_attention){
            gotoMyAttention();
        }
        if (item.getItemId() == R.id.nav_cache){
            TRouter.go(Constants.CACHE);
        }
        if (item.getItemId() == R.id.nav_recode){
            TRouter.go(Constants.HISTORY);
        }
        if (item.getItemId() == R.id.nav_setting){
            TRouter.go(Constants.SETTINGS);
        }
        mDlMainDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @CheckLogin
    private void gotoMyAttention(){
        TRouter.go(Constants.MYATTENTION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mDlMainDrawer.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                isSoftKeyBoardShow = true;
            } else {
                isSoftKeyBoardShow = false;
                if (searchView.isShowing() && !isShowTransition)
                    searchView.showView();
            }
            mDlMainDrawer.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mDlMainDrawer.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    @Override
    public void onBackPressed() {
        if (mDlMainDrawer.isDrawerOpen(GravityCompat.START)) {
            mDlMainDrawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (System.currentTimeMillis() - firstTime > 2000) {
            UiUtils.snackbarText("再按一次退出应用");
            firstTime = System.currentTimeMillis();
        } else {
            appComponent.appManager().appExit();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideKeyboard(searchView, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchView, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof MaterialSearchView)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == MatisseActivity.RESULT_OK) {
            String path = Matisse.obtainPathResult(data).get(0);
            EventBus.getDefault().post(path, EventBusTags.MINE_FRAGMENT_SET_FACE_PIC);
        }
    }

    @Subscriber(tag = EventBusTags.SET_USER_INFO)
    public void setUserInfo(User user) {
        CircleImageView img = (CircleImageView) mNvMainNavigation.getHeaderView(0).findViewById(R.id.im_face);
        TextView name = (TextView) mNvMainNavigation.getHeaderView(0).findViewById(R.id.tv_name);
        name.setText(user.getUsername());
        if (user.getIcon() != null) {
            appComponent.imageLoader().loadImage(this,
                    GlideImageConfig
                            .builder()
                            .transformation(new GlideCircleTransform(this))
                            .url(user.getIcon().getFileUrl())
                            .errorPic(R.drawable.ic_noface)
                            .imageView(img)
                            .build());
        }
        BmobQuery<MyAttentionEntity> query = new BmobQuery<MyAttentionEntity>();
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        query.order("-createdAt");
        query.findObjects(new FindListener<MyAttentionEntity>() {
            @Override
            public void done(List<MyAttentionEntity> list, BmobException e) {
                MyFollowedInfo.getInstance().setList(list);
            }
        });

    }
    @Subscriber(tag = EventBusTags.SETTING_ACTIVITY_LOG_OUT)
    public void logoutReset(String tag) {
        CircleImageView img = (CircleImageView) mNvMainNavigation.getHeaderView(0).findViewById(R.id.im_face);
        TextView name = (TextView) mNvMainNavigation.getHeaderView(0).findViewById(R.id.tv_name);
        name.setText("点击头像登录");
        appComponent.imageLoader().loadImage(this,
                GlideImageConfig
                        .builder()
                        .transformation(new GlideCircleTransform(this))
                        .load(R.drawable.ic_noface)
                        .placeholder(R.drawable.ic_noface)
                        .errorPic(R.drawable.ic_noface)
                        .imageView(img)
                        .build());
        MyFollowedInfo.getInstance().setList(null);
    }

    @Subscriber(tag = EventBusTags.MAIN_ACTIVITY_PERMISSION)
    private void requestPermission(String tag) {
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                EventBus.getDefault().post("succeed", EventBusTags.MINE_FRAGMENT_PERMISSION_BACK);
            }

            @Override
            public void onRequestPermissionFailure() {
                UiUtils.makeText(MainActivity.this, "权限被拒绝");
            }
        }, rxPermissions, mErrorHandler);
    }

    @Subscriber(tag = EventBusTags.MINE_FRAGMENT_SET_FACE_PIC)
    private void setFacePic(String path){
        CircleImageView img = (CircleImageView) mNvMainNavigation.getHeaderView(0).findViewById(R.id.im_face);
        appComponent.imageLoader().loadImage(this,
                GlideImageConfig
                        .builder()
                        .transformation(new GlideCircleTransform(this))
                        .url(path)
                        .errorPic(R.drawable.ic_noface)
                        .imageView(img)
                        .build());
    }
    @Subscriber(tag = EventBusTags.HOT_FRAGMENT_SET_VIEWPAGER)
    private void setHotPager(ViewPager viewPager){
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

}
