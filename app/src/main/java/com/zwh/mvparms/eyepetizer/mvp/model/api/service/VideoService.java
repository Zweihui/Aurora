package com.zwh.mvparms.eyepetizer.mvp.model.api.service;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.IndextVideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public interface VideoService {
    //获取分类信息
    @GET("v3/videos")
    Observable<VideoListInfo> getVideoList(@Query("start") int startCount,@Query("num") int num,@Query("categoryName") String categoryName);
    @GET("v5/index/tab/feed")
    Observable<IndextVideoListInfo> getIndexVideoList(@Query("date") long date,@Query("num") int num, @Query("udid") String udid, @Query("vc") String vc, @Query("vn") String vn, @Query("deviceModel") String deviceModel);
    @GET("v4/tabs/selected")
    Observable<IndextVideoListInfo> getMoreIndexVideoList(@Query("num") int num, @Query("page") int page,@Query("udid") String udid, @Query("vc") String vc, @Query("vn") String vn, @Query("deviceModel") String deviceModel);
    //获取排行信息
    @GET("v3/ranklist")
    Observable<VideoListInfo> getRankVideoList(@Query("strategy") String strategy);

    @GET("v4/pgcs/videoList")
    Observable<VideoListInfo> getPopularVideoList(@Query("id") int id,@Query("strategy") String strategy,@Query("start") int startCount,@Query("num") int num);
    @GET("v4/playlists/{id}/videos")
    Observable<VideoListInfo> getPlayListVideoList(@Path("id") int id,@Query("start") int startCount,@Query("num") int num);
}
