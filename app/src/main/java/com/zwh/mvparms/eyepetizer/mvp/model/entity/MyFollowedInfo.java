package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\12\1 0001.
 */

public class MyFollowedInfo {
    private static volatile MyFollowedInfo instance;
    private List<MyAttentionEntity> list;

    private MyFollowedInfo() {
    }

    public static MyFollowedInfo getInstance() {
        if (instance == null) {
            synchronized (MyFollowedInfo.class) {
                if (instance == null) {
                    instance = new MyFollowedInfo();
                }
            }
        }
        return instance;
    }

    public List<MyAttentionEntity> getList() {
        return list;
    }

    public void setList(List<MyAttentionEntity> list) {
        this.list = list;
    }


    public boolean checkFollowed(int id){
        if (list == null)
            return false;
        for (MyAttentionEntity entity :list){
            if (entity.getId() == id){
                return true;
            }
        }
        return false;
    }
}
