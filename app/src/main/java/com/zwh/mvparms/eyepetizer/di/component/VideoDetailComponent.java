package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.VideoDetailModule;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.VideoDetailActivity;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.VideoListFragment;


@ActivityScope
@Component(modules = VideoDetailModule.class, dependencies = AppComponent.class)
public interface VideoDetailComponent {
    void inject(VideoDetailActivity activity);
}