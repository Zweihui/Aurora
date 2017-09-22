package com.zwh.mvparms.eyepetizer.mvp.ui.adapter.section;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ReplyInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class ReplySection extends SectionEntity<ReplyInfo.Reply> {
    public ReplySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ReplySection(ReplyInfo.Reply reply) {
        super(reply);
    }
}
