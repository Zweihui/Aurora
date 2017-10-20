package com.zwh.mvparms.eyepetizer.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.zwh.mvparms.eyepetizer.mvp.contract.HotContract;
import com.zwh.mvparms.eyepetizer.mvp.model.HotModel;


@Module
public class HotModule {
    private HotContract.View view;

    /**
     * 构建HotModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HotModule(HotContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HotContract.View provideHotView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HotContract.Model provideHotModel(HotModel model) {
        return model;
    }
}