package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.SearchModule;

import com.zwh.mvparms.eyepetizer.mvp.ui.activity.SearchActivity;

@ActivityScope
@Component(modules = SearchModule.class, dependencies = AppComponent.class)
public interface SearchComponent {
    void inject(SearchActivity activity);
}