package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class Person extends BmobObject {
    private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
