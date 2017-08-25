package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class DataExtra {
    private HashMap<String, Object> map = new HashMap();

    public DataExtra(HashMap<String, Object> map) {
        this.map = map;
    }

    public DataExtra(String key, Object value) {
        this.map.put(key, value);
    }

    public DataExtra add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public HashMap build() {
        return this.map;
    }

    public static HashMap getExtra(String key, Object value) {
        return new DataExtra(key, value).build();
    }
}
