package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.HistoryModule;

import com.zwh.mvparms.eyepetizer.mvp.ui.activity.HistoryActivity;

@ActivityScope
@Component(modules = HistoryModule.class, dependencies = AppComponent.class)
public interface HistoryComponent {
    void inject(HistoryActivity activity);
}