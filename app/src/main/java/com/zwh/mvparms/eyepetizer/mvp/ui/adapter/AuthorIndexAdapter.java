package com.zwh.mvparms.eyepetizer.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.ui.widget.MyBanner;

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
//        addItemType(Constants.TEXTCARD_CLICK, R.layout.item_index_text_card);
//        addItemType(Constants.TEXTCARD_UNCLICK, R.layout.item_index_text_card);
//        addItemType(Constants.VIDEOSMALLCARD, R.layout.item_index_text_card);
//        addItemType(Constants.VIDEOCOLLECTIONWITHBRIEF, R.layout.item_index_text_card);
//        addItemType(Constants.DYNAMICINFOCARD, R.layout.item_index_text_card);
    }

    @Override
    protected void convert(BaseViewHolder helper, AuthorIndexInfo.ItemListBeanX item) {
        switch (helper.getItemViewType()) {
            case Constants.VIDEOCOLLECTIONOFHORIZONTALSCROLLCARD:
                MyBanner banner = helper.getView(R.id.banner);
                BannerAdapter bannerAdapter = new BannerAdapter(item.getData().getItemList());
                banner.setData(item.getData().getItemList());
                break;
            case Constants.TEXTCARD_CLICK:
//                helper.getView(R.id.iv_right).setVisibility(View.VISIBLE);
//                helper.getView(R.id.rl_text_card).setClickable(true);
                break;
            case Constants.TEXTCARD_UNCLICK:
//                helper.getView(R.id.iv_right).setVisibility(View.GONE);
//                helper.getView(R.id.rl_text_card).setClickable(false);
                break;
            case Constants.VIDEOSMALLCARD:
                break;
            case Constants.VIDEOCOLLECTIONWITHBRIEF:
                break;
            case Constants.DYNAMICINFOCARD:
                break;
        }
    }
}