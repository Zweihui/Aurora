package com.zwh.mvparms.eyepetizer.mvp.ui.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.jess.arms.di.component.AppComponent;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.PrefFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

@Router(Constants.SETTINGS)
public class SettingsActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_settings;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initToolBar();
        getFragmentManager().beginTransaction().replace(R.id.fl_container, new PrefFragment()).commit();
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
