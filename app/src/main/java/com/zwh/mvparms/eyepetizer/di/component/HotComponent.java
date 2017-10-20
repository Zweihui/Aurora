package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.HotModule;

import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.HotFragment;

@ActivityScope
@Component(modules = HotModule.class, dependencies = AppComponent.class)
public interface HotComponent {
    void inject(HotFragment fragment);
}