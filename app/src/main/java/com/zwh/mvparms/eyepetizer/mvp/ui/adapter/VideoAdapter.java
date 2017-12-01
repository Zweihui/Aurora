package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.apt.TRouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

import static android.R.attr.data;
import static android.R.attr.pointerIcon;

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
        Glide.with(context).load(item.getData().getCover().getFeed())
                .into(imgMian);
        mAppComponent.imageLoader().loadImage(context,
                GlideImageConfig
                        .builder()
                        .url(item.getData().getCover().getFeed())
                        .imageView(imgMian)
                        .build());
        try {
            ((App)context.getApplicationContext())
                    .getAppComponent().imageLoader().loadImage(context,
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
        if (item.getData().getAuthor()!=null){
            helper.getView(R.id.img_author).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoAuthorDetail(view,helper.getLayoutPosition());
                }
            });
        }
    }

    @SingleClick
    private void gotoAuthorDetail(View view ,int position){
        TRouter.go(Constants.AUTHORDETAIL, new DataExtra(Constants.AUTHOR_ID, this.getData().get(position).getData().getAuthor().getId()).build());
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
