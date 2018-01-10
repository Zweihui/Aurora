package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideCircleTransform;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.DateUtils;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017\11\28 0028.
 */

public class AuthorDynamicAdapter extends BaseQuickAdapter<AuthorDynamicInfo.Dynamic,BaseViewHolder> {

    public AuthorDynamicAdapter(@LayoutRes int layoutResId, @Nullable List<AuthorDynamicInfo.Dynamic> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AuthorDynamicInfo.Dynamic item) {
        Context context = helper.itemView.getContext();
        AppComponent mAppComponent = ((App)context.getApplicationContext())
                .getAppComponent();
        helper.setText(R.id.tv_author,item.getData().getUser().getNickname());
        helper.setText(R.id.tv_operation,item.getData().getText());
        helper.setText(R.id.tv_date, DateUtils.DateToString(new Date(item.getData().getCreateDate()),DateUtils.DATE_TO_STRING_SHORT_PATTERN));
        ImageView face = helper.getView(R.id.iv_face);
        mAppComponent.imageLoader().loadImage(context,
                ImageConfigImpl
                        .builder()
                        .url(item.getData().getUser().getAvatar())
                        .imageView(face)
                        .build());
        if ("video".equals(item.getData().getDynamicType())){
            helper.setText(R.id.tv_video_name,item.getData().getSimpleVideo().getTitle());
            helper.setText(R.id.tv_video_detail,getDetailStr(item.getData().getSimpleVideo().getDuration()+"",item.getData().getSimpleVideo().getCategory()));
            helper.getView(R.id.ctl_video).setVisibility(View.VISIBLE);
            helper.getView(R.id.ctl_follow).setVisibility(View.GONE);
            ImageView iv6 = helper.getView(R.id.imageView6);
            mAppComponent.imageLoader().loadImage(context,
                    ImageConfigImpl
                            .builder()
                            .url(item.getData().getSimpleVideo().getCover().getFeed())
                            .imageView(iv6)
                            .build());
        }
        if ("follow".equals(item.getData().getDynamicType())){
            helper.setText(R.id.tv_follow_name,item.getData().getBriefCard().getTitle());
            helper.setText(R.id.tv_follow_detail,item.getData().getBriefCard().getDescription());
            helper.getView(R.id.ctl_video).setVisibility(View.GONE);
            helper.getView(R.id.ctl_follow).setVisibility(View.VISIBLE);
            ImageView iv7 = helper.getView(R.id.imageView7);
            mAppComponent.imageLoader().loadImage(context,
                    ImageConfigImpl
                            .builder()
                            .transformation(new GlideCircleTransform())
                            .url(item.getData().getBriefCard().getIcon())
                            .imageView(iv7)
                            .build());
        }
    }

    private String getDetailStr(String duration,String category){
        int seconds = Integer.parseInt(duration);
        int temp=0;
        StringBuffer sb=new StringBuffer();
        temp = seconds/3600;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600/60;
        sb.append((temp<10)?"0"+temp+":":""+temp+":");

        temp=seconds%3600%60;
        sb.append((temp<10)?"0"+temp:""+temp);
        String detail = "#"+category+" / "+sb.toString();
        return detail;
    }

}
