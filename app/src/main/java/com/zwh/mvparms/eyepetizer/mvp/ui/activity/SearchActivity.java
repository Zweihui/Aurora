package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerSearchComponent;
import com.zwh.mvparms.eyepetizer.di.module.SearchModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.SearchContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.SearchPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.SearchAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.FlowLayout;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.transition.FadeOutTransition;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.transition.SimpleTransitionListener;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.transition.ViewFader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Router(Constants.SEARCH)
public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.flowlayout)
    FlowLayout mFlowLayout;
    @BindView(R.id.image_search_back)
    ImageView imageSearchBack;
    @BindView(R.id.edit_text_search)
    EditText editTextSearch;
    @BindView(R.id.clearSearch)
    ImageView clearSearch;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.list_root)
    LinearLayout listRoot;
    @BindView(R.id.tv_tip)
    TextView tvTip;


    private ViewFader fader = new ViewFader();
    private View footView;
    private RxPermissions mRxPermissions;
    private List<VideoListInfo.Video> mSearchResult = new ArrayList<>();
    private SearchAdapter adapter;
    private int page = 1;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchModule(new SearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolBar();
        mSwipeRefresh.setEnabled(false);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        imageSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
            }
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    clearSearch.setVisibility(View.VISIBLE);
                }else {
                    clearSearch.setVisibility(View.GONE);
                }
            }
        });
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    mPresenter.getSearchList(null,editTextSearch.getText().toString(),false);
                    listRoot.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        mFlowLayout.setOnFlowLayoutItemClickListener(new FlowLayout.OnFlowLayoutItemClickListener() {
            @Override
            public void onClick(View v, String s) {
                editTextSearch.setText(s);
                mPresenter.getSearchList(null,editTextSearch.getText().toString(),false);
                listRoot.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextSearch,InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
            }
        });
        mPresenter.getHotWords();
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }


    @Override
    public void showLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public void finish() {
        if (supportsTransitions()) {
            exitTransitionWithAction(new Runnable() {
                @Override
                public void run() {
                    SearchActivity.super.finish();
                    overridePendingTransition(0, 0);
                }
            });
        } else {
            super.finish();
        }
    }

    private void exitTransitionWithAction(final Runnable endingAction) {
        Transition transition = FadeOutTransition.withAction(new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                endingAction.run();
            }
        });

        TransitionManager.beginDelayedTransition(mToolbar, transition);
        fader.hideContentOf(mToolbar);
        TransitionManager.beginDelayedTransition(mContent, new Fade(Fade.OUT));
        mContent.setVisibility(View.GONE);
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void setListData(List<VideoListInfo.Video> list,boolean isLoadMore,int total) {
        if (adapter == null){
            mFlowLayout.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            footView = getLayoutInflater().inflate(R.layout.item_video_detail_foot, null, false);
            adapter = new SearchAdapter(R.layout.item_video_search,mSearchResult);
            mRecyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    mPresenter.getSearchList((page*10)+"",editTextSearch.getText().toString(),true);
                }
            },mRecyclerView);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    gotoDetail(view,position);
                }
            });
        }
        if (StringUtils.isEmpty(list)&&adapter!=null){
            if (!isLoadMore)
                tvTip.setText("「"+editTextSearch.getText().toString()+"」"+"搜索结果共"+total+"个");
            if (adapter.getFooterLayoutCount()<1){
                adapter.addFooterView(footView);
            }
            adapter.setEnableLoadMore(false);
            return;
        }
        if (isLoadMore){
            adapter.addData(list);
            adapter.loadMoreComplete();
            page++;
        }else {
            tvTip.setText("「"+editTextSearch.getText().toString()+"」"+"搜索结果共"+total+"个");
            adapter.setNewData(list);
            adapter.setEnableLoadMore(true);
        }
    }

    private void gotoDetail(View view, int position) {
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, adapter.getData().get(position)).build(),view.findViewById(R.id.iv_bg));
    }

    @Override
    public void setHotWordData(List<String> list) {
        mFlowLayout.displayUI(list);
    }
}
