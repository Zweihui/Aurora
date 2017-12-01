package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.VideoListActivityModule;

import com.zwh.mvparms.eyepetizer.mvp.ui.activity.VideoListActivity;

@ActivityScope
@Component(modules = VideoListActivityModule.class, dependencies = AppComponent.class)
public interface VideoListActivityComponent {
    void inject(VideoListActivity activity);
}