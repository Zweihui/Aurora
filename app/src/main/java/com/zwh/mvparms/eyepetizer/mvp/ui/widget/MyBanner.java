package com.zwh.mvparms.eyepetizer.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jess.arms.utils.UiUtils;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2017/11/25.
 */

public class MyBanner extends RelativeLayout{

    private Context mContext;
    private LayoutInflater inflater;
    private SpeedRecyclerView mRecyclerView;
    private RecyclerSnapHelper snapHelper;
    private BannerAdapter adapter;
    private LinearLayout mPointContainerLl;
    protected List<VideoListInfo.Video> mData = new ArrayList<>();

    public MyBanner(Context context) {
        this(context,null);
    }

    public MyBanner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyBanner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        //初始化RecyclerView
        mRecyclerView = new SpeedRecyclerView(mContext);
        mRecyclerView.setId(R.id.banner_recycler);
        //以matchParent的方式将RecyclerView填充到控件容器中
        addView(mRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        //创建指示器容器的相对布局
        RelativeLayout indicatorContainerRl = new RelativeLayout(mContext);
        //设置指示器容器Padding
        indicatorContainerRl.setPadding(UiUtils.dip2px(mContext,2), 0, UiUtils.dip2px(mContext,8), 0);
        //初始化指示器容器的布局参数
        LayoutParams indicatorContainerLp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置指示器容器内的子view的布局方式
        indicatorContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicatorContainerLp.addRule(RelativeLayout.BELOW,R.id.banner_recycler);
        //将指示器容器添加到父View中
        addView(indicatorContainerRl, indicatorContainerLp);
        //初始化存放点的线性布局
        mPointContainerLl = new LinearLayout(mContext);
        //设置线性布局的id
        mPointContainerLl.setId(R.id.banner_pointContainerId);
        //设置线性布局的方向
        mPointContainerLl.setOrientation(LinearLayout.HORIZONTAL);
        //设置点容器的布局参数
        LayoutParams pointContainerLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pointContainerLp.addRule(RelativeLayout.CENTER_IN_PARENT);
        //将点容器存放到指示器容器中
        indicatorContainerRl.addView(mPointContainerLl, pointContainerLp);
    }

    /**
     * 初始化点
     * 这样的做法，可以使在刷新获数据的时候提升性能
     */
    private void initPoints() {

        int childCount = mPointContainerLl.getChildCount();
        int dataSize = mData.size();
        int offset = dataSize - childCount;
        if (offset == 0)
            return;
        if (offset > 0) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(UiUtils.dip2px(mContext,4), UiUtils.dip2px(mContext,8), UiUtils.dip2px(mContext,4), UiUtils.dip2px(mContext,8));
            ImageView imageView;
            for (int i = 0; i < offset; i++) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(lp);
                imageView.setImageResource(R.drawable.selector_banner_point);
                imageView.setEnabled(false);
                if (i==0){
                    imageView.setEnabled(true);
                }
                mPointContainerLl.addView(imageView);
            }
            return;
        }
        if (offset < 0) {
            mPointContainerLl.removeViews(dataSize, -offset);
        }
    }

    public void setData(List<VideoListInfo.Video> list){
        this.mData.addAll(list);
        adapter = new BannerAdapter(mData);
        snapHelper = new RecyclerSnapHelper();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    for (int i = 0; i < mPointContainerLl.getChildCount(); i++) {
                        if (mPointContainerLl.getChildAt(i)!=null)
                        mPointContainerLl.getChildAt(i).setEnabled(false);
                    }
                    if (mPointContainerLl.getChildAt(Math.abs(snapHelper.getPosition()%mData.size()))!=null){
                        if (snapHelper.getPosition()%mData.size()<0){
                            mPointContainerLl.getChildAt(5+snapHelper.getPosition()%mData.size()).setEnabled(true);
                        }else {
                            mPointContainerLl.getChildAt(snapHelper.getPosition()%mData.size()).setEnabled(true);
                        }
                    }
                }
            }
        });
        if (mRecyclerView.getOnFlingListener() == null){
            snapHelper.attachToRecyclerView(mRecyclerView);
        }
        notifyDataHasChanged();
    }

    /**
     * 通知数据已经放生改变
     */
    public void notifyDataHasChanged() {
        initPoints();
        adapter.notifyDataSetChanged();
        mRecyclerView.getLayoutManager().scrollToPosition(10);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollBy(10,0);
            }
        });
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.getLayoutManager().scrollToPosition(0);
            }
        },3000);
    }
}
