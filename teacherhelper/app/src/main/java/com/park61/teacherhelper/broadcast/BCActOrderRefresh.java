package com.park61.teacherhelper.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 支付成功返回刷新订单列表广播
 * Created by shubei on 2017/7/5.
 */
public class BCActOrderRefresh {

    public static final String BCActOrderRefresh_ACTION = "ACTION_REFRESH_ORDER";

    private BroadcastReceiver broadcastReceiver;

    public BCActOrderRefresh(final OnReceiveDoneLsner lsner) {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lsner.onGot(intent);
            }
        };
    }

    public interface OnReceiveDoneLsner {
        void onGot(Intent intent);
    }

    public void registerReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BCActOrderRefresh_ACTION);
        mContext.registerReceiver(broadcastReceiver, filter);
    }

    public void unregisterReceiver(Context mContext) {
        mContext.unregisterReceiver(broadcastReceiver);
    }

}
