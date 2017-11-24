package com.zwh.mvparms.eyepetizer.mvp.model.api.service;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.AttentionInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public interface AttentionService {
    //获取位关注的视频信息
    @GET("v4/tabs/follow")
    Observable<AttentionInfo> getAttentionVideoList(@Query("start") int startCount, @Query("num") int num);
//    @GET("v4/tabs/selected")
//    Observable<IndextVideoListInfo> getIndexVideoList(@Query("lastStartId") int lastStartId, @Query("udid") String udid, @Query("vc") String vc, @Query("vn") String vn, @Query("deviceModel") String deviceModel);
//    @GET("v4/tabs/selected")
//    Observable<IndextVideoListInfo> getMoreIndexVideoList(@Query("num") int num, @Query("page") int page, @Query("udid") String udid, @Query("vc") String vc, @Query("vn") String vn, @Query("deviceModel") String deviceModel);
//    //获取排行信息
//    @GET("v3/ranklist")
//    Observable<VideoListInfo> getRankVideoList(@Query("strategy") String strategy);
}
