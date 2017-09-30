package com.zwh.mvparms.eyepetizer.mvp.model.api.service;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/28 0028.
 */

public interface SearchService {

    //获取搜索热词
    @GET("v3/queries/hot")
    Observable<List<String>> getHotWord();
    //获取搜索结果
    @GET("v1/search")
    Observable<VideoListInfo> getSearchList(@Query("start") String start,@Query("query") String query);
}
