package com.suse.networktest.utils;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
