package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideCircleTransform;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.StringUtils;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

/**
 * Created by Administrator on 2017\11\28 0028.
 */

public class AuthorAlbumAdapter extends BaseQuickAdapter<AuthorAlbumInfo.Album,BaseViewHolder> {

    public AuthorAlbumAdapter(@LayoutRes int layoutResId, @Nullable List<AuthorAlbumInfo.Album> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AuthorAlbumInfo.Album item) {
        Context context = helper.itemView.getContext();
        AppComponent mAppComponent = ((App)context.getApplicationContext())
                .getAppComponent();
        ImageView imgAutor = helper.getView(R.id.iv_avatar);
        try {
            mAppComponent.imageLoader().loadImage(imgAutor.getContext(),
                    ImageConfigImpl
                            .builder()
                            .transformation(new GlideCircleTransform())
                            .url(StringUtils.replaceNull(item.getData().getHeader().getIcon()))
                            .imageView(helper.getView(R.id.iv_avatar))
                            .build());
        }catch (NullPointerException e){

        }
        helper.getView(R.id.btn_attention).setVisibility(View.GONE);
        helper.setText(R.id.tv_name,item.getData().getHeader().getTitle())
                .setText(R.id.tv_desc,item.getData().getHeader().getDescription());
        RecyclerView recyclerView = helper.getView(R.id.inside_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        BaseQuickAdapter adapter = new AttentionInsideAdapter(R.layout.item_attention_horizontal,item.getData().getItemList());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                gotoDetail(view,position,item.getData().getItemList().get(position));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @SingleClick
    private void gotoDetail(View view, int position,VideoListInfo.Video video) {
        TRouter.go(Constants.VIDEO, new DataExtra(Constants.VIDEO_INFO, video).build(), view.findViewById(R.id.iv_bg));
    }
}
