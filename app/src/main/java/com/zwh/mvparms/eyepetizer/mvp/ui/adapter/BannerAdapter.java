package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apt.TRouter;
import com.jess.arms.base.App;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.StringUtils;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.MarginAdapterHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2017/11/25.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder>  {
    private List<VideoListInfo.Video> mList = new ArrayList<>();
    private MarginAdapterHelper mCardAdapterHelper = new MarginAdapterHelper();

    public BannerAdapter(List<VideoListInfo.Video> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_banner_inside, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int fakePosition = position % mList.size();
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        AppComponent mAppComponent = ((App)holder.mImageView.getContext().getApplicationContext())
                .getAppComponent();
        Context context = holder.mImageView.getContext();
        try {
            ((App)context.getApplicationContext())
                    .getAppComponent().imageLoader().loadImage(mAppComponent.appManager().getCurrentActivity() == null
                            ? mAppComponent.application() : mAppComponent.appManager().getCurrentActivity(),
                    GlideImageConfig
                            .builder()
                            .url(StringUtils.replaceNull(mList.get(fakePosition).getData().getCover().getFeed()))
                            .imageView(holder.mImageView)
                            .build());
        }catch (NullPointerException e){

        }
        holder.mLlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoDetail(view,fakePosition);
            }
        });
        holder.mTvTitle.setText(mList.get(fakePosition).getData().getTitle());
        holder.mTvdesc.setText(getDetailStr(mList.get(fakePosition)));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
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

    @SingleClick
    private void gotoDetail(View view,int position){
        TRouter.go(Constants.VIDEO,new DataExtra(Constants.VIDEO_INFO, mList.get(position)).build(),view.findViewById(R.id.iv_bg));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  ImageView mImageView;
        public  LinearLayout mLlRoot;
        public  TextView mTvTitle;
        public  TextView mTvdesc;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_bg);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_name);
            mTvdesc = (TextView) itemView.findViewById(R.id.tv_desc);
            mLlRoot = (LinearLayout) itemView.findViewById(R.id.ll_root);
        }

    }

}
