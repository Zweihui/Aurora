package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
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
 * Created by Administrator on 2017/9/29 0029.
 */

public class SearchAdapter extends BaseQuickAdapter<VideoListInfo.Video,BaseViewHolder> {
    public SearchAdapter(@LayoutRes int layoutResId, @Nullable List<VideoListInfo.Video> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoListInfo.Video item) {
        AppComponent mAppComponent = ((App)helper.getView(R.id.iv_bg).getContext().getApplicationContext())
                .getAppComponent();
        ImageView imgbg = helper.getView(R.id.iv_bg);
        Context context = imgbg.getContext();
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(item.getData().getCover().getFeed())
                        .imageView(imgbg)
                        .build());
        helper.setText(R.id.tv_name,item.getData().getTitle())
                .setText(R.id.tv_desc,getDetailStr(item));
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
