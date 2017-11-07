package com.zwh.mvparms.eyepetizer.mvp.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.jess.arms.utils.DeviceUtils;
import com.jess.arms.utils.UiUtils;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;

import org.simple.eventbus.EventBus;

import static com.jess.arms.utils.DeviceUtils.NETTYPE_CMNET;
import static com.jess.arms.utils.DeviceUtils.NETTYPE_CMWAP;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = DeviceUtils.getNetworkType(context);
            if (netWorkState == NETTYPE_CMWAP||netWorkState == NETTYPE_CMNET){
                EventBus.getDefault().post("",EventBusTags.NET_CHANGE_FLOW);
            }
        }
    }
}
