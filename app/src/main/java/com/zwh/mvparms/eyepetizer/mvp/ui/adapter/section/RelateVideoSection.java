package com.zwh.mvparms.eyepetizer.mvp.ui.adapter.section;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

/**
 * Created by mac on 2017/9/3.
 */

public class RelateVideoSection extends SectionEntity<VideoListInfo.Video> {
    public RelateVideoSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public RelateVideoSection(VideoListInfo.Video item) {
        super(item);
    }
}
