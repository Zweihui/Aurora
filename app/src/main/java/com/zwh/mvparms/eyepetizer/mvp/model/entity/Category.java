package com.zwh.mvparms.eyepetizer.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class Category implements Parcelable {

    /**
     * id : 20
     * name : 音乐
     * alias : null
     * description : 全球最酷、最炫、最有态度的音乐集合
     * bgPicture : http://img.kaiyanapp.com/9279c17b4da5ba5e7e4f21afb5bb0a74.jpeg
     * bgColor :
     * headerImage : http://img.kaiyanapp.com/8c1e48b68c2fd63f49c3c49d732c5af6.png
     */

    private int id;
    private int authorId;
    private String name;
    private String alias;
    private String description;
    private String bgPicture;
    private String bgColor;
    private String headerImage;

    public Category(String description, String name) {
        this.name = name;
        this.description = description;
    }

    public Category(Parcel in) {
        id = in.readInt();
        authorId = in.readInt();
        name = in.readString();
        alias = in.readString();
        description = in.readString();
        bgPicture = in.readString();
        bgColor = in.readString();
        headerImage = in.readString();
    }

    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBgPicture() {
        return bgPicture;
    }

    public void setBgPicture(String bgPicture) {
        this.bgPicture = bgPicture;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(authorId);
        parcel.writeString(name);
        parcel.writeString(alias);
        parcel.writeString(description);
        parcel.writeString(bgPicture);
        parcel.writeString(bgColor);
        parcel.writeString(headerImage);
    }

    public static final Parcelable.Creator<Category> CREATOR = new Creator<Category>() {

        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
