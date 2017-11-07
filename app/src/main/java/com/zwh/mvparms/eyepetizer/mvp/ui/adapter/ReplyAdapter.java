package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.section.ReplySection;

import org.simple.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class ReplyAdapter extends BaseSectionQuickAdapter<ReplySection,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public ReplyAdapter(int layoutResId, int sectionHeadResId, List<ReplySection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ReplySection item) {
        helper.setText(R.id.tv_name,item.t.getData().getText());
        helper.getView(R.id.iv_arrow).setVisibility(View.GONE);
        helper.addOnClickListener(R.id.iv_arrow_right);
        if (helper.getLayoutPosition() == 0){
            helper.getView(R.id.iv_arrow_right).setVisibility(View.VISIBLE);
            helper.getView(R.id.view).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.view).setVisibility(View.GONE);
            helper.getView(R.id.iv_arrow_right).setVisibility(View.GONE);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplySection item) {
        ImageView img = helper.getView(R.id.iv_pic);
        AppComponent mAppComponent = ((App)img.getContext().getApplicationContext())
                .getAppComponent();
        if (item.t.getData().getUser()!=null){
            mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                            ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                    GlideImageConfig
                            .builder()
                            .transformation(new GlideCircleTransform(img.getContext()))
                            .url(item.t.getData().getUser().getAvatar())
                            .imageView(img)
                            .build());
        }
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        if (item.t.getData().getUser()!=null){
            helper.setText(R.id.tv_nickname,item.t.getData().getUser().getNickname());
        }
        helper.setText(R.id.tv_comment,item.t.getData().getMessage())
                .setText(R.id.tv_good_num,item.t.getData().getLikeCount()+"")
                .setText(R.id.tv_reply_time,df.format(item.t.getData().getCreateTime()));
    }
}
