package com.zwh.mvparms.eyepetizer.mvp.model.api.service;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017\11\22 0022.
 */

public interface AuthorDetailService {
    @GET("v4/pgcs/videoList")
    Observable<VideoListInfo> getAuthorVideoList(@Query("id") int id,@Query("start") int startCount, @Query("num") int num);
    @GET("v4/pgcs/detail/playlist")
    Observable<AuthorAlbumInfo> getAuthorAlbumList(@Query("id") int id,@Query("start") int startCount, @Query("num") int num);
    @GET("v5/userInfo/tab/dynamics")
    Observable<AuthorDynamicInfo> getAuthorDynamicList(@Query("id") int id, @Query("start") int startCount, @Query("num") int num, @Query("userType") String type);
    @GET("v5/userInfo/tab")
    Observable<AuthorTabsInfo> getAuthorTabs(@Query("id") int id, @Query("userType") String type);
    @GET("v5/userInfo/tab/index")
    Observable<AuthorIndexInfo> getAuthorIndexInfo(@Query("id") int id, @Query("userType") String type);
    //获取分享信息
    @GET("v2/share")
    Observable<ShareInfo> getShareInfo(@Query("sourceType") String sourceType, @Query("itemType") String itemType, @Query("identity") int identity);

}
