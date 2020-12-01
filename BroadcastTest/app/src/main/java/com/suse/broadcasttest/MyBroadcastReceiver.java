package com.suse.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "receiver in MyBroadcastReceiver", Toast.LENGTH_SHORT).show();
        //用于截断有序广播，后面的广播接收器将无法接收到这条广播
        abortBroadcast();
    }
}