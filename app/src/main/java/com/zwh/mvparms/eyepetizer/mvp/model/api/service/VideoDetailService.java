package com.zwh.mvparms.eyepetizer.mvp.model.api.service;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.ReplyInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public interface VideoDetailService {
    //获取相关视频信息
    @GET("v4/video/related")
    Observable<VideoListInfo> getRelateVideoInfo(@Query("id") int id);
    //获取二级相关视频信息
    @GET("v4/video/{path}")
    Observable<VideoListInfo> getSecondRelateVideoInfo(@Path("path") String path,@Query("id") int id,@Query("start") int startCount,@Query("num") int num);
    //获取全部评论信息
    @GET("v2/replies/video")
    Observable<ReplyInfo> getAllReplyInfo(@Query("videoId") int videoId);
    //获取更多最新评论信息
    @GET("v2/replies/video")
    Observable<ReplyInfo> getMoreReplyInfo(@Query("lastId") int lastId,@Query("videoId") int videoId,@Query("type") String type);
    //获取分享信息
    @GET("v2/share")
    Observable<ShareInfo> getShareInfo(@Query("sourceType") String sourceType, @Query("itemType") String itemType, @Query("identity") int identity);
}
