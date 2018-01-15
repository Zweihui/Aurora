package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.os.Bundle;

import com.zwh.annotation.apt.Extra;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
@Router(Constants.AUTHOR)
public class AboutMimeActivity extends BaseWebActivity {
    @Extra(Constants.AUTHOR_TYPE)
    public int type;   //0 github主页，1 github issue

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (type == 0){
            mWebView.loadUrl(Constants.GITHUB_URL);
            setTitle("关于作者");
        }
        if (type == 1){
            mWebView.loadUrl(Constants.GITHUB_URL_ISSUES);
            setTitle("意见反馈");
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
