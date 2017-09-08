package com.zwh.mvparms.eyepetizer.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.RelateVideoInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

import io.reactivex.Observable;


public interface VideoDetailContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setData(RelateVideoInfo info);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        Observable<RelateVideoInfo> getRelateVideoInfo(int id);
    }
}