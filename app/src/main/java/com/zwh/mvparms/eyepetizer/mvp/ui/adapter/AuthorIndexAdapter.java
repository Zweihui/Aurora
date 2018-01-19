package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideCircleTransform;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.DateUtils;
import com.jess.arms.utils.StringUtils;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.app.utils.CommonUtils;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.FollowButton;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.MyBanner;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017\11\24 0024.
 */

public class AuthorIndexAdapter extends BaseMultiItemQuickAdapter<AuthorIndexInfo.ItemListBeanX,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AuthorIndexAdapter(List<AuthorIndexInfo.ItemListBeanX> data) {
        super(data);
        addItemType(Constants.VIDEOCOLLECTIONOFHORIZONTALSCROLLCARD, R.layout.item_index_banner);
        addItemType(Constants.TEXTCARD_CLICK, R.layout.item_index_text_card_foot);
        addItemType(Constants.TEXTCARD_UNCLICK, R.layout.item_index_text_card);
        addItemType(Constants.VIDEOSMALLCARD, R.layout.item_video_detail_group_content);
        addItemType(Constants.VIDEOCOLLECTIONWITHBRIEF, R.layout.item_index_likely);
        addItemType(Constants.DYNAMICINFOCARD, R.layout.item_index_dynamic);
    }

    @Override
    protected void convert(BaseViewHolder helper, AuthorIndexInfo.ItemListBeanX item) {
        Context context = helper.itemView.getContext();
        AppComponent mAppComponent = ((App)context.getApplicationContext())
                .getAppComponent();
        helper.setIsRecyclable(true);
        switch (helper.getItemViewType()) {
            case Constants.VIDEOCOLLECTIONOFHORIZONTALSCROLLCARD:
                MyBanner banner = helper.getView(R.id.banner);
                helper.setText(R.id.tv_text,item.getData().getHeader().getTitle());
                helper.addOnClickListener(R.id.rl_recent_update);
                BannerAdapter bannerAdapter = new BannerAdapter(item.getData().getItemList());
                banner.setData(item.getData().getItemList());
                helper.setIsRecyclable(false);
                break;
            case Constants.TEXTCARD_CLICK:
                helper.setText(R.id.text_card_title,item.getData().getText());
                helper.getView(R.id.iv_right).setVisibility(View.VISIBLE);
                View view = helper.getView(R.id.rl_text_card_foot);
                if (item.getData().getText().contains("动态")){
                    view.setId(R.id.rl_text_card_foot_dynamic);
                    helper.addOnClickListener(R.id.rl_text_card_foot_dynamic);
                }
                if (item.getData().getText().contains("专辑")){
                    view.setId(R.id.rl_text_card_foot_album);
                    helper.addOnClickListener(R.id.rl_text_card_foot_album);
                }
                if (item.getData().getText().contains("欢迎")){
                    view.setId(R.id.rl_text_card_foot_popular);
                    helper.addOnClickListener(R.id.rl_text_card_foot_popular);
                }
                break;
            case Constants.TEXTCARD_UNCLICK:
                AuthorIndexInfo.ItemListBeanX.DataBeanX data3 = (AuthorIndexInfo.ItemListBeanX.DataBeanX) item.getData();
                helper.setText(R.id.text_card_title,data3.getText());
                helper.getView(R.id.iv_right).setVisibility(View.GONE);
                break;
            case Constants.VIDEOSMALLCARD:
                AuthorIndexInfo.ItemListBeanX.DataBeanX video = item.getData();
                ImageView iv_left = helper.getView(R.id.iv_left);
                helper.addOnClickListener(R.id.ctl_root);
                mAppComponent.imageLoader().loadImage(context,
                    ImageConfigImpl
                            .builder()
                            .url(video.getCover().getFeed())
                            .imageView(iv_left)
                            .build());
                helper.setText(R.id.tv_title,video.getTitle())
                        .setText(R.id.tv_type,getDetailStr(video.getDuration()+"",video.getCategory()));
                break;
            case Constants.VIDEOCOLLECTIONWITHBRIEF:
                ImageView imgAutor = helper.getView(R.id.iv_avatar);
                try {
                    mAppComponent.imageLoader().loadImage(context,
                            ImageConfigImpl
                                    .builder()
                                    .transformation(new GlideCircleTransform())
                                    .url(StringUtils.replaceNull(item.getData().getHeader().getIcon()))
                                    .imageView(helper.getView(R.id.iv_avatar))
                                    .build());
                }catch (NullPointerException e){

                }
                if (item.getData().getHeader().getDescription().contains("专辑")){
                    helper.getView(R.id.ctl_likely).setId(R.id.ctl_author_index_album);
                    helper.addOnClickListener(R.id.ctl_author_index_album);
                }else {
                    helper.getView(R.id.ctl_likely).setId(R.id.ctl_author_index_like);
                    helper.addOnClickListener(R.id.ctl_author_index_like);
                    helper.addOnClickListener(R.id.btn_attention);
                    FollowButton button = helper.getView(R.id.btn_attention);
                    button.setState(CommonUtils.checkFollowed(context,item.getData().getHeader().getId())
                            ?FollowButton.FOLLOWED:FollowButton.UNFOLLOWED);
                    MyAttentionEntity attention = new MyAttentionEntity();
                    attention.setId(item.getData().getHeader().getId());
                    attention.setTitle((item.getData().getHeader().getTitle()));
                    attention.setDescription((item.getData().getHeader().getDescription()));
                    attention.setIcon((item.getData().getHeader().getIcon()));
                    button.setOnFollowClickListener(new FollowButton.onFollowClickListener() {
                        @Override
                        public void onFollowed() {

                        }

                        @Override
                        public void onUnFollowed() {

                        }
                    },attention);
                }
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
                break;
            case Constants.DYNAMICINFOCARD:
                helper.addOnClickListener(R.id.ctl_dynamic);
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
                break;
        }
    }

    @SingleClick
    private void gotoDetail(View view, int position,VideoListInfo.Video video) {
        TRouter.go(Constants.VIDEO, new DataExtra(Constants.VIDEO_INFO, video).build(), view.findViewById(R.id.iv_bg));
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