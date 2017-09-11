package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.RelateVideoInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.section.RelateVideoSection;

import java.util.List;

/**
 * Created by mac on 2017/9/3.
 */

public class RelateVideoAdapter extends BaseSectionQuickAdapter<RelateVideoSection,BaseViewHolder> {


    public RelateVideoAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, RelateVideoSection item) {
        helper.setText(R.id.tv_name,item.t.getData().getText());
    }

    @Override
    protected void convert(BaseViewHolder helper, RelateVideoSection item) {
        helper.setText(R.id.tv_title,item.t.getData().getTitle())
                .setText(R.id.tv_type,getDetailStr(item.t));
        ImageView img = helper.getView(R.id.iv_left);
        AppComponent mAppComponent = ((App)img.getContext().getApplicationContext())
                .getAppComponent();
        Context context = img.getContext();
        ((App)context.getApplicationContext())
                .getAppComponent().imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(item.t.getData().getData().getCover().getFeed())
                        .imageView(img)
                        .build());
    }

    private String getDetailStr(RelateVideoInfo.ItemListBean item){
        String duration = item.getData().getData().getDuration()+"";
        int seconds = Integer.parseInt(duration);
        int temp=0;
        StringBuffer sb=new StringBuffer();
        temp = seconds/3600;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600/60;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600%60;
        sb.append((temp<10)?"0"+temp:""+temp);
        String detail = "#"+item.getData().getData().getCategory()+" / "+sb.toString();
        return detail;
    }
}
