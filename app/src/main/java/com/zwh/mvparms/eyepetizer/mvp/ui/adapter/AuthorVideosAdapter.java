package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

/**
 * Created by Administrator on 2017\11\28 0028.
 */

public class AuthorVideosAdapter extends BaseQuickAdapter<VideoListInfo.Video,BaseViewHolder> {
    public AuthorVideosAdapter(@LayoutRes int layoutResId, @Nullable List<VideoListInfo.Video> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoListInfo.Video item) {
        helper.setText(R.id.tv_title,item.getData().getTitle())
                .setText(R.id.tv_type,getDetailStr(item));
        ImageView img = helper.getView(R.id.iv_left);
        AppComponent mAppComponent = ((App)img.getContext().getApplicationContext())
                .getAppComponent();
        Context context = img.getContext();
        mAppComponent.imageLoader().loadImage(context,
                ImageConfigImpl
                        .builder()
                        .url(item.getData().getCover().getFeed())
                        .imageView(img)
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
