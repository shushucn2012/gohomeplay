package com.park61.teacherhelper.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 专家关注之后返回刷新列表广播
 * Created by shubei on 2017/7/5.
 */
public class BCSkListRefresh {

    public static final String BCSkListRefresh_ACTION = "ACTION_REFRESH_SKLIST";

    private BroadcastReceiver broadcastReceiver;

    public BCSkListRefresh(final OnReceiveDoneLsner lsner) {
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
        filter.addAction(BCSkListRefresh_ACTION);
        mContext.registerReceiver(broadcastReceiver, filter);
    }

    public void unregisterReceiver(Context mContext) {
        mContext.unregisterReceiver(broadcastReceiver);
    }

}
