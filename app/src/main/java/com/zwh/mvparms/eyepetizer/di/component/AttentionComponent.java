package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.AttentionModule;

import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.AttentionFragment;

@ActivityScope
@Component(modules = AttentionModule.class, dependencies = AppComponent.class)
public interface AttentionComponent {
    void inject(AttentionFragment fragment);
}