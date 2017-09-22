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
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class CategoryAdapter extends BaseQuickAdapter<Category,BaseViewHolder> {


    public CategoryAdapter(@LayoutRes int layoutResId, @Nullable List<Category> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        AppComponent mAppComponent = ((App)helper.getView(R.id.iv_bg).getContext().getApplicationContext())
                .getAppComponent();
        ImageView bg = helper.getView(R.id.iv_bg);
        helper.setText(R.id.tv_name,item.getName());
        mAppComponent.imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                GlideImageConfig
                        .builder()
                        .url(item.getBgPicture())
                        .imageView(bg)
                        .build());
    }
}
