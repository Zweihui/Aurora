package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.os.Bundle;

import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
@Router(Constants.AUTHOR)
public class AboutMimeActivity extends BaseWebActivity {


    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mWebView.loadUrl(Constants.GITHUB_URL);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
