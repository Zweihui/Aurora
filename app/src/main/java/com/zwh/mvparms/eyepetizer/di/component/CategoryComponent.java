package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zwh.mvparms.eyepetizer.di.module.CategoryModule;

import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.CategoryFragment;

@ActivityScope
@Component(modules = CategoryModule.class, dependencies = AppComponent.class)
public interface CategoryComponent {
    void inject(CategoryFragment fragment);
}