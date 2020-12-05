package com.suse.servicebestpractice;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class DownloadTask2 extends AsyncTaskLoader {
    public DownloadTask2(@NonNull Context context) {
        super(context);
    }
    @Nullable
    @Override
    public Object loadInBackground() {
        return null;
    }
}
