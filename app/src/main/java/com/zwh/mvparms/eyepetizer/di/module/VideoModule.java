package com.zwh.mvparms.eyepetizer.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.zwh.mvparms.eyepetizer.mvp.contract.VideoContract;
import com.zwh.mvparms.eyepetizer.mvp.model.VideoModel;


@Module
public class VideoModule {
    private VideoContract.View view;

    /**
     * 构建VideoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VideoModule(VideoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VideoContract.View provideVideoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VideoContract.Model provideVideoModel(VideoModel model) {
        return model;
    }
}