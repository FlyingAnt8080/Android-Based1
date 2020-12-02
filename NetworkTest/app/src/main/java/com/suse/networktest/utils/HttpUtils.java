package com.suse.networktest.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class HttpUtils {
    /**
     * 基于HttpURLConnection封装网络请求工具类
     */
    public static void sendHttpRequest(final String address,
                                       final  HttpCallbackListener listener){
        new Thread(()->{
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if (listener != null){
                        //回调onFinish方法
                        listener.onFinish(response.toString());
                    }
                } finally {
                    if (reader != null){
                        reader.close();
                    }
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            } catch (Exception e) {
                if (listener != null){
                    listener.onError(e);
                }
            }
        }).start();
    }

    /**
     * 基于OkHttp3封装的网络请求
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
