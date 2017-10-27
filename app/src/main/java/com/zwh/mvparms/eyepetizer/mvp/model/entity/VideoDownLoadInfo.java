package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
@Entity(nameInDb = "DOWNLOAD")
public class VideoDownLoadInfo implements Serializable,DaoEntity{

    private static final long serialVersionUID = 123456788L;

    @Property(nameInDb = "_id")
    @Id
    private Long id;
    @Property
    private String body;
    @Property
    private Long currentBytes; //已下载大小
    @Property
    private Long contentLength;//全部大小
    @Property
    private Boolean finish; //进度是否完成
    @Property
    private String path; //全路径

    @Transient
    private VideoListInfo.Video.VideoData video;


    @Generated(hash = 890312406)
    public VideoDownLoadInfo(Long id, String body, Long currentBytes,
            Long contentLength, Boolean finish, String path) {
        this.id = id;
        this.body = body;
        this.currentBytes = currentBytes;
        this.contentLength = contentLength;
        this.finish = finish;
        this.path = path;
    }

    @Generated(hash = 1738706251)
    public VideoDownLoadInfo() {
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

    public Long getCurrentBytes() {
        return currentBytes;
    }

    public void setCurrentBytes(Long currentBytes) {
        this.currentBytes = currentBytes;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public VideoListInfo.Video.VideoData getVideo() {
        return video;
    }

    public void setVideo(VideoListInfo.Video.VideoData video) {
        this.video = video;
    }

    @Override
    public String getDbName() {
        return "DOWNLOAD";
    }
}
