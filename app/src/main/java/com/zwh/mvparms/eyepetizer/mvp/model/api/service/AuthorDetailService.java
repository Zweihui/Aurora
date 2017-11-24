package com.zwh.mvparms.eyepetizer.mvp.model.api.service;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017\11\22 0022.
 */

public interface AuthorDetailService {
    @GET("v4/pgcs/videoList")
    Observable<VideoListInfo> getAuthorVideoList(@Query("start") int startCount, @Query("num") int num);
    @GET("v5/userInfo/tab")
    Observable<AuthorTabsInfo> getAuthorTabs(@Query("id") int startCount, @Query("userType") String type);
}
