package com.suse.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;

public class Notifications {
    private final static int NOTIFICATION_SIMPLE = 0;
    public final static int NOTIFICATION_BIG_PICTURE_STYLE = 1;
    public final static int NOTIFICATION_BIG_TEXT_STYLE = 2;

    private static volatile Notifications instance = null;

    private Notifications(){

    }

    public static Notifications getInstance(){
        if (instance==null){
            synchronized (Notifications.class){
                if (instance==null){
                    instance = new Notifications();
                }
            }
        }
        return instance;
    }

    /**
     * 发送一个简单的通知
     * @param context
     */
    public void sendSimpleNotification(Context context,NotificationManager nm){
        Intent intent = new Intent(context,NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);
        Notification notification = new NotificationCompat.Builder(context,NotificationChannels.HIGH)
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pi)
                //设置状态栏上的通知图标自动取消  方法一
                .setAutoCancel(true)
                //弹通知的时候播放音频
                //.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                //弹通知的时候让手机震动
                //控制震动声明权限
                //停止1秒，震动1秒，停止1秒，震动1秒
                //.setVibrate(new long[]{0,1000,0,1000})
                //弹通知的时候让呼吸灯亮
                //参数1：灯颜色，参数2：灯亮的时长，参数3：灯暗的时长
                //.setLights(Color.GREEN,1000,1000)
                //如果不想麻烦，就用系统默认的效果
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                //用来构建具体的富文本信息的，如长文字、大图片等。
                //设置长文字
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("AsyncTask was intended to enable proper and easy use of the UI thread. However, the most common use case was for integrating into UI, and that would cause Context leaks, missed callbacks, or crashes on configuration changes. It also has inconsistent behavior on different versions of the platform, swallows exceptions from doInBackground"))
                //设置大图片
                // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.big_image)))
                //Android5.0+才有该功能
                //设置通知优先级，共有5个等级，PRIORITY_MIN、PRIORITY_DEFAULT、PRIORITY_LOW、PRIORITY_HIGH、PRIORITY_MAX
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        nm.notify(NOTIFICATION_SIMPLE,notification);
        //设置状态栏上的通知图标自动取消  方法二
        //1 是通知id
        //manager.cancel(NOTIFICATION_SIMPLE);
    }
}
