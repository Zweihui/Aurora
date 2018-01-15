package com.zwh.mvparms.eyepetizer.mvp.ui.widget.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * Created by Administrator on 2018\1\15 0015.
 */

public class GlideEngine implements ImageEngine {
    public GlideEngine() {
    }

    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        GlideArms.with(context).asBitmap().load(uri).placeholder(placeholder).override(resize, resize).centerCrop().into(imageView);
    }

    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        GlideArms.with(context).asBitmap().load(uri).placeholder(placeholder).override(resize, resize).centerCrop().into(imageView);
    }

    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        GlideArms.with(context).asBitmap().load(uri).override(resizeX, resizeY).priority(Priority.HIGH).into(imageView);
    }

    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        GlideArms.with(context).asBitmap().load(uri).override(resizeX, resizeY).priority(Priority.HIGH).into(imageView);
    }

    public boolean supportAnimatedGif() {
        return true;
    }
}
