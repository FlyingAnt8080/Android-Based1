package com.suse.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.suse.coolweather.WeatherActivity;
import com.suse.coolweather.gson.AQI;
import com.suse.coolweather.gson.Forecasts;
import com.suse.coolweather.gson.Now;
import com.suse.coolweather.gson.Suggestions;
import com.suse.coolweather.gson.Weather;
import com.suse.coolweather.util.API;
import com.suse.coolweather.util.HttpUtil;
import com.suse.coolweather.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    private Weather mWeather;
    private static int count;
    public AutoUpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        count = 0;
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;//这是8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if (weatherString != null){
            //有缓存直接解析天气数据
            mWeather = Utility.handleWeather(weatherString);
            String locationId = mWeather.locationId;
            requestWeatherNow(locationId);
            requestWeather3Day(locationId);
            requestWeatherIndicesNow(locationId);
            requestAQI(locationId);
        }
    }

    /**
     * 请求空气质量数据
     * @param locationId
     */
    private void requestAQI(String locationId) {
        String address = API.AIR_URL + "location=" + locationId + "&key=" + API.KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    if (jsonObject.getInt("code") == 200){
                        AQI aqi = new Gson().fromJson(jsonObject.getString("now"),AQI.class);
                        mWeather.aqi = aqi;
                        count++;
                        saveToSharedPreferences();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 请求天气指数(即建议)
     * @param locationId
     */
    private void requestWeatherIndicesNow(String locationId) {
        String address = API.INDICES_NOW_URL + "location=" + locationId + "&key=" + API.KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    if (jsonObject.getInt("code") == 200){
                        Suggestions suggestions = new Gson().fromJson(responseText,Suggestions.class);
                        mWeather.suggestions = suggestions;
                        count++;
                        saveToSharedPreferences();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 请求未来3天天气情况数据
     * @param locationId
     */
    private void requestWeather3Day(String locationId) {
        String address = API.WEATHER_3_DAY_URL + "location=" + locationId + "&key=" + API.KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    if (jsonObject.getInt("code")==200){
                        Gson gson = new Gson();
                        Forecasts forecasts = gson.fromJson(responseText,Forecasts.class);
                        mWeather.forecasts = forecasts;
                        count++;
                        saveToSharedPreferences();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取实时天气情况
     * @param locationId
     */
    private void requestWeatherNow(String locationId) {
        String address = API.WEATHER_NOW_URL + "location=" + locationId + "&key=" + API.KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    if (jsonObject.getInt("code") == 200) {
                        String nowText = jsonObject.getString("now");
                        Now now = new Gson().fromJson(nowText, Now.class);
                        mWeather.now = now;
                        count++;
                        saveToSharedPreferences();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 更新每日一图
     */
    private void updateBingPic(){
        HttpUtil.sendOkHttpRequest(API.BING_PIC_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String binPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",binPic);
                editor.apply();
            }
        });
    }

    /**
     * 将天气数据保存到共享数据中
     */
    private void saveToSharedPreferences(){
        if (count == 4){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
            Gson gson = new Gson();
            String weather = gson.toJson(mWeather);
            editor.putString("weather",weather);
            editor.apply();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}