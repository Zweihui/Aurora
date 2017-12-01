package com.zwh.mvparms.eyepetizer.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ReplyInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;
import retrofit2.http.Query;


public interface VideoDetailContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setData(VideoListInfo info,boolean isShowSecond);
        void setReplyData(ReplyInfo info, boolean isLoadmore);
        void setShareData(ShareInfo info);
        void setVideoData(VideoListInfo.Video.VideoData data);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        Observable<VideoListInfo> getRelateVideoInfo(int id);
        Observable<VideoListInfo> getSecondRelateVideoInfo(String path,int id,int startCount);
        Observable<ReplyInfo> getAllReplyInfo(int videoId);
        Observable<ReplyInfo> getMoreReplyInfo(int lastId, int videoId);
        Observable<ShareInfo> getShareInfo(int identity);
        Observable<VideoListInfo.Video.VideoData> getVideoData(int id);
    }
}