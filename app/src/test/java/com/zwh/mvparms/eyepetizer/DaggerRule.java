package com.zwh.mvparms.eyepetizer;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.module.AppModule;
import com.jess.arms.di.module.ClientModule;
import com.zwh.mvparms.eyepetizer.app.GlobalConfiguration;

import org.robolectric.RuntimeEnvironment;

import it.cosenonjaviste.daggermock.DaggerMockRule;

/**
 * Created by Administrator on 2017\12\14 0014.
 */

public class DaggerRule extends DaggerMockRule<AppComponent> {
    public DaggerRule() {
        super(AppComponent.class,new AppModule(RuntimeEnvironment.application)
                ,new ClientModule(), new GlobalConfiguration());
        set(new ComponentSetter<AppComponent>() {
            @Override
            public void setComponent(AppComponent component) {

            }
        });
    }
}
