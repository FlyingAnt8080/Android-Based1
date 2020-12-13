package com.suse.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 未来天气
 */
public class Forecasts {
    @SerializedName("daily")
    public List<Forecast> forecastList;
    public class Forecast{
        @SerializedName("fxDate")
        public String date;
        //最高温度
        public String tempMax;
        //最低温度
        public String tempMin;
        @SerializedName("textDay")
        public String info;
    }
}
