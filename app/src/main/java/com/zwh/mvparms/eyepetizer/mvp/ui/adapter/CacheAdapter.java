package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
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

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class CacheAdapter extends BaseQuickAdapter<VideoDownLoadInfo,BaseViewHolder> {
    private String type;
    private int downPosition = -1;
    public CacheAdapter(@LayoutRes int layoutResId, @Nullable List<VideoDownLoadInfo> data) {
        super(layoutResId, data);
    }

    public CacheAdapter(@LayoutRes int layoutResId, @Nullable List<VideoDownLoadInfo> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoDownLoadInfo item) {
        AppComponent mAppComponent = ((App)helper.getView(R.id.iv_bg).getContext().getApplicationContext())
                .getAppComponent();
        ImageView imgMian = helper.getView(R.id.iv_bg);
        Context context = imgMian.getContext();
        SeekBar seekBar = helper.getView(R.id.sb_progress);
        seekBar.setPadding(0,0,0,0);
        helper.setText(R.id.tv_title,item.getVideo().getTitle());
        if (item.getVideo().getAuthor()!=null){
            helper.setText(R.id.tv_author,item.getVideo().getAuthor().getName());
        }
        helper.setText(R.id.tv_size, StringUtils.getPrintSize(item.getContentLength() == null ?0:item.getContentLength(),true));
        helper.addOnClickListener(R.id.iv_more);
        helper.addOnClickListener(R.id.ll_detail);
        helper.addOnClickListener(R.id.ll_progress);
        helper.addOnClickListener(R.id.ctl_layout);
        if (!type.contains("正在")){
            helper.getView(R.id.ll_detail).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.ll_detail).setVisibility(View.GONE);
        }
        if (item.getFinish()){
            helper.getView(R.id.ll_progress).setVisibility(View.GONE);
            helper.getView(R.id.tv_size).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.tv_size).setVisibility(View.GONE);
//            if (downPosition == helper.getLayoutPosition()){
//                helper.getView(R.id.ll_progress).setVisibility(View.VISIBLE);
//                helper.getView(R.id.sb_progress).setVisibility(View.VISIBLE);
//                ((SeekBar)helper.getView(R.id.sb_progress)).setProgress(item.getPercent());
//                String size = StringUtils.getPrintSize(item.getCurrentBytes() == null ?0:item.getContentLength(),false) + "/" + StringUtils.getPrintSize(item.getContentLength() == null ?0:item.getContentLength(),true);
//                helper.setText(R.id.tv_pause,size);
//            }else {
                if (item.isLineUp()){
                    helper.setText(R.id.tv_pause,"排队中");
                }else {
                    helper.setText(R.id.tv_pause,"已暂停");
                }
                helper.getView(R.id.ll_progress).setVisibility(View.VISIBLE);
                helper.getView(R.id.sb_progress).setVisibility(View.GONE);
//            }
        }
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(item.getVideo().getCover().getFeed())
                        .imageView(imgMian)
                        .build());
    }

    public void setDownPosition(int downPosition) {
        this.downPosition = downPosition;
    }
}
