package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zwh.mvparms.eyepetizer.di.module.VideoModule;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.HomeFragment;

import dagger.Component;

/**
 * Created by mac on 2017/9/16.
 */
@ActivityScope
@Component(modules = VideoModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeFragment fragment);
}
