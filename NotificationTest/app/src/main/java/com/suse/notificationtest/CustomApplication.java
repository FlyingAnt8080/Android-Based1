package com.suse.notificationtest;

import android.app.Application;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannels.createNotificationChannels(this);
    }
}
