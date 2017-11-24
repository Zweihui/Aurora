package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class MyAttentionEntity extends BmobObject{

    private static final long serialVersionUID = 123406789L;

    private int id;
    private String icon;
    private String title;
    private String description;
    private Boolean follow;
    private String userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFollow() {
        return follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
