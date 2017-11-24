package com.zwh.mvparms.eyepetizer.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;

/**
 * Created by Administrator on 2017\11\23 0023.
 */

public class AuthorIndexFragment extends BaseLazyLoadFragment {

    SwipeRefreshLayout mSwipeRefresh;
    RecyclerView mRecyclerView;


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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = (int) getArguments().get(Constants.AUTHOR_ID);
        if (savedInstanceState != null) {
            id = savedInstanceState.getInt("type");
        }
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.common_recyclerview, container, false);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void setData(Object data) {

    }

    @Override
    protected void loadData() {

    }
}
