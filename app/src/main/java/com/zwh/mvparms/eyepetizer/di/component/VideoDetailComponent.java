package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.VideoDetailModule;


@ActivityScope
@Component(modules = VideoDetailModule.class, dependencies = AppComponent.class)
public interface VideoDetailComponent {
}