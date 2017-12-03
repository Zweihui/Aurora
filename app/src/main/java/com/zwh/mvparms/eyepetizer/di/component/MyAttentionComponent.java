package com.zwh.mvparms.eyepetizer.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zwh.mvparms.eyepetizer.di.module.AttentionModule;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.MyAttentionActivity;

import dagger.Component;

/**
 * Created by mac on 2017/12/2.
 */

@ActivityScope
@Component(modules = AttentionModule.class, dependencies = AppComponent.class)
public interface MyAttentionComponent {
    void inject(MyAttentionActivity fragment);
}
