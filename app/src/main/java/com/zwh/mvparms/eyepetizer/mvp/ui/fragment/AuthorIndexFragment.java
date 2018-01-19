package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.CommonUtils;
import com.zwh.mvparms.eyepetizer.di.component.DaggerAuthorDetailIndexComponent;
import com.zwh.mvparms.eyepetizer.di.module.AuthorDetailModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.AuthorDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.AuthorDetailPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.AuthorIndexAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.FollowButton;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\11\23 0023.
 */

public class AuthorIndexFragment extends BaseLazyLoadFragment<AuthorDetailPresenter> implements AuthorDetailContract.View{

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private AuthorIndexAdapter adapter ;
    private List<AuthorIndexInfo.ItemListBeanX> data = new ArrayList<>();
    private AppComponent appComponent;

    private int id;


    public static AuthorIndexFragment newInstance(int id) {
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.AUTHOR_ID, id);
        AuthorIndexFragment fragment = new AuthorIndexFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.AUTHOR_ID, id);
        getArguments().putBundle(Constants.AUTHOR_ID, outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            id = savedInstanceState.getInt(Constants.AUTHOR_ID);
        }else {
            id = (int) getArguments().get(Constants.AUTHOR_ID);
        }
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        DaggerAuthorDetailIndexComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .authorDetailModule(new AuthorDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.common_recyclerview, container, false);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setEnabled(false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources()
                        .getDisplayMetrics()));
        adapter = new AuthorIndexAdapter(data);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (id){
                    case R.id.rl_recent_update:
                        EventBus.getDefault().post("videos", EventBusTags.AUTHOR_DETAIL_TAB);
                        break;
                    case R.id.rl_text_card_foot_dynamic:
                        EventBus.getDefault().post("dynamic", EventBusTags.AUTHOR_DETAIL_TAB);
                        break;
                    case R.id.rl_text_card_foot_album:
                        EventBus.getDefault().post("album", EventBusTags.AUTHOR_DETAIL_TAB);
                        break;
                    case R.id.ctl_root:
                        gotoDetail(view,position);
                        break;
                    case R.id.ctl_dynamic:
                        gotoDetail2(view,position);
                        break;
                    case R.id.rl_text_card_foot_popular:
                        gotoPopular(view,position);
                        break;
                    case R.id.ctl_author_index_album:
                        gotoPlaylist(view,position);
                        break;
                    case R.id.ctl_author_index_like:
                        gotoAuthorDetail(view,position);
                        break;
                }
            }

        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    @SingleClick
    private void gotoPopular(View view, int position) {
        TRouter.go(Constants.VIDEOLIST,new DataExtra(Constants.COMMON_ID, id).add(Constants.COMMON_TYPE,"popular").build(),view.findViewById(R.id.iv_bg));
    }
    @SingleClick
    private void gotoPlaylist(View view, int position) {
        TRouter.go(Constants.VIDEOLIST,new DataExtra(Constants.COMMON_ID,
                adapter.getData().get(position).getData().getHeader().getId()).add(Constants.COMMON_TYPE,adapter.getData().get(position).getData().getHeader().getTitle()).build()
                ,view.findViewById(R.id.iv_bg));
    }

    @SingleClick
    private void gotoDetail(View view,int position){
        VideoListInfo.Video  info = new VideoListInfo.Video();
        Gson gson = appComponent.gson();
        String json = gson.toJson(data.get(position).getData());
        VideoListInfo.Video.VideoData videoData = gson.fromJson(json,VideoListInfo.Video.VideoData.class);
        info.setData(videoData);
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, info).build(),view.findViewById(R.id.iv_left));
    }

    @SingleClick
    private void gotoDetail2(View view, int position) {
        if ("video".equals(data.get(position).getData().getDynamicType())){
            VideoListInfo.Video  info = new VideoListInfo.Video();
            Gson gson = appComponent.gson();
            String json = gson.toJson(data.get(position).getData().getSimpleVideo());
            VideoListInfo.Video.VideoData videoData = gson.fromJson(json,VideoListInfo.Video.VideoData.class);
            info.setData(videoData);
            TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, info).build(),view.findViewById(R.id.imageView6));
        }
    }
    @SingleClick
    private void gotoAuthorDetail(View view, int position) {
        TRouter.go(Constants.AUTHORDETAIL, new DataExtra(Constants.AUTHOR_ID, data.get(position).getData().getHeader().getId()).build());
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void setData(Object data) {

    }

    @Override
    protected void loadData() {
        mPresenter.getAuthorIndexInfo(id);
        mPresenter.getAuthorTabs(id);
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
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public void setVideosData(List<VideoListInfo.Video> videos, boolean isLoadMore) {

    }

    @Override
    public void setTabs(AuthorTabsInfo info) {
        initHeadView(info);
    }

    @Override
    public void setShareInfo(ShareInfo info) {

    }

    private void initHeadView(AuthorTabsInfo info) {
        View headView = getActivity().getLayoutInflater().inflate(R.layout.view_head_author_detail, mRecyclerView, false);
        MyAttentionEntity attention = new MyAttentionEntity();
        attention.setId(info.getPgcInfo().getId());
        attention.setTitle((info.getPgcInfo().getName()));
        attention.setDescription((info.getPgcInfo().getDescription()));
        attention.setIcon((info.getPgcInfo().getIcon()));
        FollowButton button = headView.findViewById(R.id.btn_attention);
        button.setState(CommonUtils.checkFollowed(headView.getContext(),id) ? FollowButton.FOLLOWED:FollowButton.UNFOLLOWED);
        button.setOnFollowClickListener(new FollowButton.onFollowClickListener() {
            @Override
            public void onFollowed() {

            }

            @Override
            public void onUnFollowed() {

            }
        },attention);
        ImageView bg = headView.findViewById(R.id.iv_bg);
        ImageView face = headView.findViewById(R.id.civ_face);
        TextView name = headView.findViewById(R.id.tv_author_name);
        TextView follow = headView.findViewById(R.id.num_folowed);
        TextView desc = headView.findViewById(R.id.tv_desc);
        TextView videoNum = headView.findViewById(R.id.tv_video_num);
        TextView collectNum = headView.findViewById(R.id.tv_collect_num);
        TextView shareNum = headView.findViewById(R.id.tv_share_num);
        follow.setText(info.getPgcInfo().getFollowCount()+" 被关注");
        name.setText(info.getPgcInfo().getName());
        desc.setText(info.getPgcInfo().getDescription());
        videoNum.setText(info.getPgcInfo().getVideoCount()+"");
        collectNum.setText(info.getPgcInfo().getCollectCount()+"");
        shareNum.setText(info.getPgcInfo().getShareCount()+"");
        if (info.getPgcInfo().getIcon()!=null){
            appComponent.imageLoader().loadImage(getActivity(),
                    ImageConfigImpl
                            .builder()
                            .url(info.getPgcInfo().getIcon())
                            .imageView(face)
                            .dontAnimate(true)
                            .build());
        }
        if (info.getPgcInfo().getCover()!=null){
            appComponent.imageLoader().loadImage(getActivity(),
                    ImageConfigImpl
                            .builder()
                            .url(info.getPgcInfo().getCover())
                            .imageView(bg)
                            .build());
        }else {
           bg.setImageResource(R.drawable.profile_cover);
        }
        adapter.setHeaderView(headView);
    }

    @Override
    public void setIndexInfo(AuthorIndexInfo info) {
        data.addAll(info.getItemList());
        adapter.setNewData(data);
        View footView = getActivity().getLayoutInflater().inflate(R.layout.item_video_detail_foot, mRecyclerView, false);
        adapter.addFooterView(footView);
    }

    @Override
    public void setAuthorAlbumInfo(List<AuthorAlbumInfo.Album> itemList, boolean isLoadMore) {

    }

    @Override
    public void setAuthorDynamicInfo(List<AuthorDynamicInfo.Dynamic> itemList, boolean isLoadMore) {

    }
}
