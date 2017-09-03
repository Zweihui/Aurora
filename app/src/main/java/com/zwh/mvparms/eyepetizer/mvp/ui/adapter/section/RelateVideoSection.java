package com.zwh.mvparms.eyepetizer.mvp.ui.adapter.section;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.RelateVideoInfo;

/**
 * Created by mac on 2017/9/3.
 */

public class RelateVideoSection extends SectionEntity<RelateVideoInfo.ItemListBean> {
    public RelateVideoSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public RelateVideoSection(RelateVideoInfo.ItemListBean itemListBean) {
        super(itemListBean);
    }
}
