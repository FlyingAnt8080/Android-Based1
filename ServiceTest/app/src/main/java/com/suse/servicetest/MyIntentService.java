package com.suse.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;

/**
 * 注意：Android8.0（即API26）以后IntentService被废弃，换用JobIntentService
 */
public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";

    public MyIntentService() {
       super("MyIntentService");
    }

    /**
     * 在该方法执行具体的逻辑，该方法在子线程中运行
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "Thread id is "+Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy executed");
    }
}
