package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.widget.imageloader.glide.GlideCircleTransform;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.FollowButton;

import java.util.List;

/**
 * Created by Administrator on 2017\11\22 0022.
 */

public class AurhorListAdapter extends BaseQuickAdapter<MyAttentionEntity,BaseViewHolder> {
    public AurhorListAdapter(@LayoutRes int layoutResId, @Nullable List<MyAttentionEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyAttentionEntity item) {
        ImageView imgAutor = helper.getView(R.id.img_icon);
        Context context = imgAutor.getContext();
        AppComponent mAppComponent = ((App)context.getApplicationContext())
                .getAppComponent();
        mAppComponent.imageLoader().loadImage(context,
                GlideImageConfig
                        .builder()
                        .transformation(new GlideCircleTransform(context))
                        .url(item.getIcon())
                        .imageView(imgAutor)
                        .build());
        helper.setText(R.id.tv_name,item.getTitle())
                .setText(R.id.tv_desc,item.getDescription());
        FollowButton button = helper.getView(R.id.btn_attention);
        button.setState(FollowButton.FOLLOWED);
        button.setCanClick(false);
    }
}
