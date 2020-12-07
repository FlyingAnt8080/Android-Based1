package com.suse.networktest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.suse.networktest.MyApplication;

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
        if (!isNetworkAvailable()){
            Toast.makeText(MyApplication.getContext(), "network is unavailable", Toast.LENGTH_SHORT).show();
        }
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

    //判断网络是否可用
    private static boolean isNetworkAvailable() {
        Context context = MyApplication.getContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null){
            return networkInfo.isAvailable();
        }
        return false;
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
