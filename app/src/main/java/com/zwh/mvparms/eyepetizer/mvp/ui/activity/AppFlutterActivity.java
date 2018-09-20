package com.zwh.mvparms.eyepetizer.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.apt.TRouter;
import com.google.gson.Gson;
import com.zwh.annotation.apt.Router;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import io.flutter.app.FlutterActivityDelegate;
import io.flutter.app.FlutterActivityDelegate.ViewFactory;
import io.flutter.app.FlutterActivityEvents;
import io.flutter.facade.Flutter;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterView;
import io.flutter.view.FlutterView.Provider;

/**
 * Created by Zhangwh on 2018/9/7.
 */
@Router(Constants.FLUTTER)
public class AppFlutterActivity extends AppCompatActivity implements Provider, PluginRegistry, ViewFactory {

    String CHANNEL = "com.zwh.mvparms.eyepetizer/main";
    private FlutterView flutterView;
    private Gson gson;

    private final FlutterActivityDelegate delegate = new FlutterActivityDelegate(this, this);
    private final FlutterActivityEvents eventDelegate;
    private final Provider viewProvider;
    private final PluginRegistry pluginRegistry;

    public AppFlutterActivity() {
        this.eventDelegate = this.delegate;
        this.viewProvider = this.delegate;
        this.pluginRegistry = this.delegate;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        FlutterMain.startInitialization(this);
        super.onCreate(savedInstanceState);
        this.eventDelegate.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        setContentView(R.layout.activity_flutter);
        final FrameLayout layout = findViewById(R.id.fl_content);
        flutterView = Flutter.createView(this,getLifecycle(),"mainRoute");
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT);
        layout.addView(flutterView,params);
//        final FlutterView.FirstFrameListener[] listeners = new FlutterView.FirstFrameListener[1];
//        listeners[0] = () -> {
//            flutterView.setVisibility(View.VISIBLE);
//            flutterView.removeFirstFrameListener(listeners[0]);
//        };
//        flutterView.addFirstFrameListener(listeners[0]);
        registerFlutterPlugin();
    }

    private void registerFlutterPlugin() {
        gson = new Gson();
        new MethodChannel(flutterView, CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("gotoVideo")) {
                String video = call.argument("video");
                if (video != null){
                    VideoListInfo.Video videoInfo =
                            gson.fromJson(video,VideoListInfo.Video.class);
                    if (videoInfo.getData() == null){
                        VideoListInfo.Video.VideoData videoData = gson.fromJson(video,VideoListInfo.Video.VideoData.class);
                        videoInfo.setData(videoData);
                    }
                    TRouter.go(Constants.FLUTTER_VIDEO,new DataExtra(Constants.VIDEO_INFO, videoInfo).build());
                }
            } else {
                result.notImplemented();
            }
        });
    }

    public FlutterView getFlutterView() {
        return this.viewProvider.getFlutterView();
    }

    public FlutterView createFlutterView(Context context) {
        return null;
    }

    public FlutterNativeView createFlutterNativeView() {
        return null;
    }

    public boolean retainFlutterNativeView() {
        return false;
    }

    public final boolean hasPlugin(String key) {
        return this.pluginRegistry.hasPlugin(key);
    }

    public final <T> T valuePublishedByPlugin(String pluginKey) {
        return this.pluginRegistry.valuePublishedByPlugin(pluginKey);
    }

    public final Registrar registrarFor(String pluginKey) {
        return this.pluginRegistry.registrarFor(pluginKey);
    }

    protected void onStart() {
        super.onStart();
        this.eventDelegate.onStart();
    }

    protected void onResume() {
        super.onResume();
        this.eventDelegate.onResume();
    }

    protected void onDestroy() {
        this.eventDelegate.onDestroy();
        super.onDestroy();
    }

    public void onBackPressed() {
        if(!this.eventDelegate.onBackPressed()) {
            super.onBackPressed();
        }

    }

    protected void onStop() {
        this.eventDelegate.onStop();
        super.onStop();
    }

    protected void onPause() {
        super.onPause();
        this.eventDelegate.onPause();
    }

    protected void onPostResume() {
        super.onPostResume();
        this.eventDelegate.onPostResume();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        this.eventDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!this.eventDelegate.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    protected void onNewIntent(Intent intent) {
        this.eventDelegate.onNewIntent(intent);
    }

    public void onUserLeaveHint() {
        this.eventDelegate.onUserLeaveHint();
    }

    public void onTrimMemory(int level) {
        this.eventDelegate.onTrimMemory(level);
    }

    public void onLowMemory() {
        this.eventDelegate.onLowMemory();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.eventDelegate.onConfigurationChanged(newConfig);
    }
}
