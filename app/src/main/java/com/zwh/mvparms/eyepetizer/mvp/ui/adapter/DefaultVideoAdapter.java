package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class DefaultVideoAdapter extends BaseQuickAdapter<VideoListInfo.Video,BaseViewHolder> {
    public DefaultVideoAdapter(@LayoutRes int layoutResId, @Nullable List<VideoListInfo.Video> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoListInfo.Video item) {

    }
}
