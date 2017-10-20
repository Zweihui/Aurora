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
import com.jess.arms.utils.DateUtils;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDaoEntity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class HistoryAdapter extends BaseQuickAdapter<VideoDaoEntity,BaseViewHolder> {
    public HistoryAdapter(@LayoutRes int layoutResId, @Nullable List<VideoDaoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoDaoEntity item) {
        AppComponent mAppComponent = ((App)helper.getView(R.id.iv_bg).getContext().getApplicationContext())
                .getAppComponent();
        ImageView imgMian = helper.getView(R.id.iv_bg);
        Context context = imgMian.getContext();
        SeekBar seekBar = helper.getView(R.id.progress);
        helper.setText(R.id.tv_title,item.getVideo().getTitle());
        if (item.getVideo().getAuthor()!=null){
            helper.setText(R.id.tv_author,item.getVideo().getAuthor().getName());
        }
        helper.setText(R.id.tv_date, DateUtils.DateToString(item.getDate(),DateUtils.DATE_TO_STRING_SHORT_PATTERN));
        helper.setText(R.id.tv_time, DateUtils.MsToString(item.getStartTime()));
        DecimalFormat df=new DecimalFormat("0.00");
        int progress = (int) (((float)item.getTotalTime()*100)/item.getTotalTime());
        seekBar.setProgress(progress);
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(item.getVideo().getCover().getFeed())
                        .imageView(imgMian)
                        .build());
    }
}
