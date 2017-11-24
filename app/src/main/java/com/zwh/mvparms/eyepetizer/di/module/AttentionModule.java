package com.zwh.mvparms.eyepetizer.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.zwh.mvparms.eyepetizer.mvp.contract.AttentionContract;
import com.zwh.mvparms.eyepetizer.mvp.model.AttentionModel;


@Module
public class AttentionModule {
    private AttentionContract.View view;

    /**
     * 构建AttentionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AttentionModule(AttentionContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AttentionContract.View provideAttentionView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AttentionContract.Model provideAttentionModel(AttentionModel model) {
        return model;
    }
}