package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

public class VideoAdapter extends BaseQuickAdapter<VideoListInfo.Video,BaseViewHolder>{
    public VideoAdapter(@LayoutRes int layoutResId, @Nullable List<VideoListInfo.Video> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoListInfo.Video item) {
        AppComponent  mAppComponent = ((App)helper.getView(R.id.img_main).getContext().getApplicationContext())
                .getAppComponent();
        ImageView imgMian = helper.getView(R.id.img_main);
        ImageView imgAutor = helper.getView(R.id.img_author);
        Context context = imgMian.getContext();
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(item.getData().getCover().getFeed())
                        .imageView(imgMian)
                        .build());
        try {
            ((App)context.getApplicationContext())
                    .getAppComponent().imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                            ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                    GlideImageConfig
                            .builder()
                            .transformation(new GlideCircleTransform(context))
                            .url(StringUtils.replaceNull(item.getData().getAuthor().getIcon()))
                            .imageView(helper.getView(R.id.img_author))
                            .build());
        }catch (NullPointerException e){

        }
        helper.setText(R.id.title,item.getData().getTitle())
                .setText(R.id.detail,getDetailStr(item));
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
