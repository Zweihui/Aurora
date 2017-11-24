package com.zwh.mvparms.eyepetizer.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.zwh.mvparms.eyepetizer.mvp.contract.AuthorDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.AuthorDetailModel;


@Module
public class AuthorDetailModule {
    private AuthorDetailContract.View view;

    /**
     * 构建AuthorDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AuthorDetailModule(AuthorDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AuthorDetailContract.View provideAuthorDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AuthorDetailContract.Model provideAuthorDetailModel(AuthorDetailModel model) {
        return model;
    }
}