package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.AuthorDetailModule;

import com.zwh.mvparms.eyepetizer.mvp.ui.activity.AuthorDetailActivity;

@ActivityScope
@Component(modules = AuthorDetailModule.class, dependencies = AppComponent.class)
public interface AuthorDetailComponent {
    void inject(AuthorDetailActivity activity);
}