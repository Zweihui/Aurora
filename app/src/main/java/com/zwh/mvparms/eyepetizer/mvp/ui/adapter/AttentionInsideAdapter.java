package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/11/15 0015.
 */

public class AttentionInsideAdapter extends BaseQuickAdapter<VideoListInfo.Video,BaseViewHolder> {
    public AttentionInsideAdapter(@LayoutRes int layoutResId, @Nullable List<VideoListInfo.Video> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoListInfo.Video item) {
        AppComponent mAppComponent = ((App)helper.getView(R.id.iv_bg).getContext().getApplicationContext())
                .getAppComponent();
        ImageView imgBg = helper.getView(R.id.iv_bg);
        helper.setText(R.id.tv_title,item.getData().getTitle())
                .setText(R.id.tv_detail,getDetailStr(item));
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(item.getData().getCover().getFeed())
                        .imageView(imgBg)
                        .build());
    }


    private String getDetailStr(VideoListInfo.Video item){
        String duration = item.getData().getDuration()+"";
        int seconds = Integer.parseInt(duration);
        int temp=0;
        StringBuffer sb=new StringBuffer();
        temp = seconds/3600;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600/60;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600%60;
        sb.append((temp<10)?"0"+temp:""+temp);
        String detail = "#"+item.getData().getCategory()+" / "+sb.toString();
        return detail;
    }
}
