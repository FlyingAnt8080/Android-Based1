package com.suse.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 当前天气情况类
 */
public class Now {
    @SerializedName("obsTime")
    public String updateTime;

    @SerializedName("temp")
    public String temperature;

    @SerializedName("text")
    public String info;
}
