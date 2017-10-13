package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.SplashModule;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.SplashActivity;


@ActivityScope
@Component(modules = SplashModule.class, dependencies = AppComponent.class)
public interface SplashComponent {
    void inject(SplashActivity activity);
}