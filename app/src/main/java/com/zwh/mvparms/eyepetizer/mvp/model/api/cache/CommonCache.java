package com.zwh.mvparms.eyepetizer.mvp.model.api.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

/**
 * Created by jess on 8/30/16 13:53
 * Contact with jess.yan.effort@gmail.com
 */
public interface CommonCache {



    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<User>>> getUsers(Observable<List<User>> users, DynamicKey idLastUserQueried, EvictProvider evictProvider);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<VideoListInfo>> getVideoList(Observable<VideoListInfo> videolistInfo, DynamicKey idLastUserQueried, EvictProvider evictProvider);

    @LifeCache(duration = 3, timeUnit = TimeUnit.DAYS)
    Observable<Reply<List<Category>>> getCategories(Observable<List<Category>> categories);

}
