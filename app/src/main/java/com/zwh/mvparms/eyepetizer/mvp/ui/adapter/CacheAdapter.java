package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDownLoadInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class CacheAdapter extends BaseQuickAdapter<VideoDownLoadInfo,BaseViewHolder> {
    public CacheAdapter(@LayoutRes int layoutResId, @Nullable List<VideoDownLoadInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoDownLoadInfo item) {
        AppComponent mAppComponent = ((App)helper.getView(R.id.iv_bg).getContext().getApplicationContext())
                .getAppComponent();
        ImageView imgMian = helper.getView(R.id.iv_bg);
        Context context = imgMian.getContext();
        SeekBar seekBar = helper.getView(R.id.progress);
        seekBar.setPadding(0,0,0,0);
        helper.setText(R.id.tv_title,item.getVideo().getTitle());
        if (item.getVideo().getAuthor()!=null){
            helper.setText(R.id.tv_author,item.getVideo().getAuthor().getName());
        }
        helper.setText(R.id.tv_size, StringUtils.getPrintSize(item.getContentLength()));
        helper.addOnClickListener(R.id.iv_more);
        helper.addOnClickListener(R.id.ll_detail);
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(item.getVideo().getCover().getFeed())
                        .imageView(imgMian)
                        .build());
    }
}
