package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.StringUtils;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.app.utils.RxUtils;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.CacheActivity;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.CacheAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by mac on 2017/10/28.
 */

public class CacheFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private CacheAdapter adapter;
    private List<VideoDownLoadInfo> data = new ArrayList<>();
    private String type;
    private Gson mGson;

    public static CacheFragment newInstance(String type) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.TYPE, type);
        CacheFragment fragment = new CacheFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        mGson = appComponent.gson();
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_recyclerview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setEnabled(false);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        adapter = new CacheAdapter(R.layout.item_cache, data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        getDateFromDb();
    }


    private void getDateFromDb() {
        Observable.create((ObservableOnSubscribe<List<VideoDownLoadInfo>>) e -> {
            VideoDownLoadInfo entity = new VideoDownLoadInfo();
            List<VideoDownLoadInfo> list = GreenDaoHelper.getInstance().create(entity.getDbName())
                    .getMaster().newSession()
                    .getVideoDownLoadInfoDao().queryBuilder()
                    .list();
            List<VideoDownLoadInfo> infolist = new ArrayList<VideoDownLoadInfo>();
            if (!StringUtils.isEmpty(list)) {
                for (VideoDownLoadInfo entity1 : list) {
                    entity1.setVideo(mGson.fromJson(entity1.getBody(), VideoListInfo.Video.VideoData.class));
                    infolist.add(entity1);
                }
            }
            e.onNext(infolist);
        }).compose(RxUtils.applySchedulersWithLifeCycle((CacheActivity) getActivity()))
                .subscribe(new Consumer<List<VideoDownLoadInfo>>() {
                               @Override
                               public void accept(@NonNull List<VideoDownLoadInfo> list) throws Exception {
                                   adapter.setNewData(list);
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        }
                );
        ;
    }

    @Override
    public void setData(Object data) {

    }

}
