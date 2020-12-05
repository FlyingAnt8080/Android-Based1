package com.suse.notificationtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import java.util.Arrays;

/**
 * 用于创建通知渠道
 * 注意：Android 8.0+(API 26+)环境下需要将通知放入特定的渠道才能正常显示
 */
public class NotificationChannels {

    public final static String HIGH = "high";
    public final static String DEFAULT = "default";
    public final static String LOW = "low";
    public final static String MIN = "min";

    public static void createNotificationChannels(Context context){
        //判断当前在此硬件设备上运行的软件的SDK版本是否为Android 8.0+(API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            CharSequence highChanelName = context.getString(R.string.channel_high);
            CharSequence defaultChanelName = context.getString(R.string.channel_default);
            CharSequence lowChanelName = context.getString(R.string.channel_low);
            CharSequence minChanelName = context.getString(R.string.channel_min);

            NotificationChannel highChanel = new NotificationChannel(HIGH,highChanelName,NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel defaultChanel = new NotificationChannel(DEFAULT,defaultChanelName,NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel lowChanel = new NotificationChannel(LOW,lowChanelName,NotificationManager.IMPORTANCE_LOW);
            NotificationChannel minChanel = new NotificationChannel(MIN,minChanelName,NotificationManager.IMPORTANCE_MIN);

            nm.createNotificationChannels(Arrays.asList(highChanel,defaultChanel,lowChanel,minChanel));
        }
    }
}
