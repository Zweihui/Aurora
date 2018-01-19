package com.zwh.mvparms.eyepetizer.app.utils;

import android.content.Context;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;

import java.util.List;

/**
 * Created by Administrator on 2018\1\18 0018.
 */

public class CommonUtils {

    public static boolean checkFollowed(Context context,int id) {
        List<MyAttentionEntity> list = getFollowedInfo(context);
        if (list == null)
            return false;
        for (MyAttentionEntity entity :list){
            if (entity.getId() == id){
                return true;
            }
        }
        return false;
    }
    public static List<MyAttentionEntity> getFollowedInfo(Context context) {
        Cache cache = ArmsUtils.obtainAppComponentFromContext(context).extras();
        List<MyAttentionEntity> list = (List<MyAttentionEntity>) cache.get(Constants.CACHE_FOLLOWED_INFO);
        return list;
    }
}
