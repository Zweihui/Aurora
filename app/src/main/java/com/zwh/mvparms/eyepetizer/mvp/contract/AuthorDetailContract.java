package com.zwh.mvparms.eyepetizer.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

import io.reactivex.Observable;


public interface AuthorDetailContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setVideosData(List<VideoListInfo.Video> videos, boolean isLoadMore);
        void setTabs(AuthorTabsInfo info);
        void setIndexInfo(AuthorIndexInfo info);

        void setAuthorAlbumInfo(List<AuthorAlbumInfo.Album> itemList, boolean isLoadMore);

        void setAuthorDynamicInfo(List<AuthorDynamicInfo.Dynamic> itemList, boolean isLoadMore);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<VideoListInfo> getAuthorVideoList(int id,int start);
        Observable<AuthorTabsInfo> getAuthorTabs(int startCount);
        Observable<AuthorIndexInfo> getAuthorIndexInfo(int id);
        Observable<AuthorDynamicInfo> getAuthorDynamicList(int id, int startCount);
        Observable<AuthorAlbumInfo> getAuthorAlbumList(int id, int startCount);
    }
}
