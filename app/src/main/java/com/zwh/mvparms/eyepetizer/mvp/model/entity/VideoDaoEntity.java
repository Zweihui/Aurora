package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * Created by Administrator on 2017/10/19 0019.
 */
@Entity(nameInDb = "VIDEO")
public class VideoDaoEntity extends BmobObject implements Serializable,DaoEntity{

    private static final long serialVersionUID = 123456789L;

    @Property(nameInDb = "_id")
    @Id
    private Long id;
    @Property
    private String body;

    @Property
    private Integer totalTime;
    @Property
    private Integer startTime;
    @Property
    private String shareInfo;
    @Property
    private Date date;
    @Transient
    public String userId;
    @Transient
    private VideoListInfo.Video.VideoData video;


    @Generated(hash = 1004013563)
    public VideoDaoEntity(Long id, String body, Integer totalTime,
            Integer startTime, String shareInfo, Date date) {
        this.id = id;
        this.body = body;
        this.totalTime = totalTime;
        this.startTime = startTime;
        this.shareInfo = shareInfo;
        this.date = date;
    }

    @Generated(hash = 571052758)
    public VideoDaoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public VideoListInfo.Video.VideoData getVideo() {
        return video;
    }

    public void setVideo(VideoListInfo.Video.VideoData video) {
        this.video = video;
    }

    public String getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(String shareInfo) {
        this.shareInfo = shareInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getDbName() {
        return "VIDEO";
    }
}
