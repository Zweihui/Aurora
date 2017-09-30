package com.zwh.mvparms.eyepetizer.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.zwh.mvparms.eyepetizer.mvp.contract.SearchContract;
import com.zwh.mvparms.eyepetizer.mvp.model.SearchModel;


@Module
public class SearchModule {
    private SearchContract.View view;

    /**
     * 构建SearchModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SearchModule(SearchContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SearchContract.View provideSearchView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SearchContract.Model provideSearchModel(SearchModel model) {
        return model;
    }
}