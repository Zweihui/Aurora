package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by Administrator on 2017/10/19 0019.
 */
@Entity(nameInDb = "VIDEO")
public class VideoDaoEntity implements Serializable,DaoEntity{

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
    private Date date;
    @Transient
    private VideoListInfo.Video.VideoData video;


    @Generated(hash = 516995707)
    public VideoDaoEntity(Long id, String body, Integer totalTime,
            Integer startTime, Date date) {
        this.id = id;
        this.body = body;
        this.totalTime = totalTime;
        this.startTime = startTime;
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

    @Override
    public String getDbName() {
        return "VIDEO";
    }
}
