package com.suse.coolweather.util;

public class API {
    //省市区访问地址
    public static final String LOCATION_BASE_URL = "http://guolin.tech/api/china";

    //和风天气需要的key
    public static final String KEY = "7a7156199c294e64baac56ff07caafba";

    //天气数据访问基地址
    public static final String WEATHER_BASE_URL = "https://devapi.qweather.com/v7";

    //查询县或区所对应id的网络地址
    public static final String LOCATION_ID_URL = "https://geoapi.qweather.com/v2/city/lookup?";

    //查询当前天气情况网络地址
    public static final String WEATHER_NOW_URL = WEATHER_BASE_URL+"/weather/now?";

    //查询最近三天天气预报网络地址
    public static final String WEATHER_3_DAY_URL = WEATHER_BASE_URL+"/weather/3d?";

    //查询当天指标参数(各种建议)网络地址
    public static final String INDICES_NOW_URL = WEATHER_BASE_URL+"/indices/1d?type=1,2,8&";

    //https://devapi.qweather.com/v7/air/now?location=101270106&key=7a7156199c294e64baac56ff07caafba
    //查询当前空气质量参数网络地址
    public static final String AIR_URL = WEATHER_BASE_URL + "/air/now?";

    //获取每日一图的网络地址
    public static final String BING_PIC_URL = "http://guolin.tech/api/bing_pic";
}
