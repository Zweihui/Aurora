package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zwh.mvparms.eyepetizer.di.module.VideoDetailModule;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.FlutterVideoDetailActivity;

import dagger.Component;


@ActivityScope
@Component(modules = VideoDetailModule.class, dependencies = AppComponent.class)
public interface FlutterVideoDetailComponent {
    void inject(FlutterVideoDetailActivity activity);
}