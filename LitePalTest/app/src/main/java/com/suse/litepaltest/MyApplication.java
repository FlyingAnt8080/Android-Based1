package com.suse.litepaltest;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class MyApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        LitePal.initialize(sContext);
    }

    public static Context getContext(){
        return sContext;
    }
}
