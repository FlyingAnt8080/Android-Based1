package com.suse.broadcasttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    //网络连接状态改变广播接收器
    private NetworkChangeReceiver networkChangeReceiver;
    //本地广播接收器
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);*/
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent("com.suse.broadcasttest.MY_BROADCAST");
                //发送标准广播
                //sendBroadcast(intent);
                //发送有序广播
                //sendOrderedBroadcast(intent,null);
                Intent intent = new Intent("com.suse.broadcasttest.LOCAL_BROADCAST");
                //发送本地广播
                localBroadcastManager.sendBroadcast(intent);
            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.suse.broadcasttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(networkChangeReceiver);
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    //本地广播接收器
    class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "received local broadcast", Toast.LENGTH_SHORT).show();
        }
    }

    //网络连接状态改变广播接收器
    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo!=null&&networkInfo.isAvailable()){
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
}