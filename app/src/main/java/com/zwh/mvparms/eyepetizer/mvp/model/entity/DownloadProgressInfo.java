package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class DownloadProgressInfo implements Parcelable {
    private int currentBytes;
    private int contentLength;
    private String id;

    public DownloadProgressInfo(String id){
        this.id = id;
    }

    public static final Parcelable.Creator<DownloadProgressInfo> CREATOR = new Creator<DownloadProgressInfo>(){

        @Override
        public DownloadProgressInfo createFromParcel(Parcel source) {
            return new DownloadProgressInfo(source);
        }

        @Override
        public DownloadProgressInfo[] newArray(int size){
            return new DownloadProgressInfo[size];
        }
    };

    public int getCurrentBytes() {
        return currentBytes;
    }

    public void setCurrentBytes(int currentBytes) {
        this.currentBytes = currentBytes;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentBytes);
        dest.writeInt(this.contentLength);
        dest.writeString(this.id);
    }

    protected DownloadProgressInfo(Parcel in) {
        this.currentBytes = in.readInt();
        this.contentLength = in.readInt();
        this.id = in.readString();
    }

}
