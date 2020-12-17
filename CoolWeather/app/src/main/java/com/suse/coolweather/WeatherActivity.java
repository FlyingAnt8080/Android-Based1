package com.suse.coolweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.suse.coolweather.gson.AQI;
import com.suse.coolweather.gson.Forecasts;
import com.suse.coolweather.gson.Now;
import com.suse.coolweather.gson.Suggestions;
import com.suse.coolweather.gson.Weather;
import com.suse.coolweather.util.API;
import com.suse.coolweather.util.HttpUtil;
import com.suse.coolweather.util.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WeatherActivity extends AppCompatActivity {
    public SwipeRefreshLayout swipeRefresh;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    public DrawerLayout drawerLayout;
    private Button navButton;

    public Weather mWeather;
    //用于标志是否三个网络请求都成功返回数据
    public static int count;
    private static final String TAG = "WeatherActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            //活动的布局会显示在状态栏上面
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //将状态栏设置为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化控件
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm2_5_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        bingPicImg = findViewById(R.id.bing_pic_img);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.purple_500);
        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
        count = 0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if (weatherString != null){
            //有缓存直接解析天气数据
             mWeather = Utility.handleWeather(weatherString);
             count = -1;
             showWeatherInfo();
        } else {
            //无缓存时去服务器查询天气
            String county_name = getIntent().getStringExtra("county_name");
            mWeather = new Weather();
            weatherLayout.setVisibility(View.INVISIBLE);
            requestLocationId(county_name);
        }
        //设置背景图片
        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
        //设置下拉刷新
        swipeRefresh.setOnRefreshListener(()->{
            requestWeather(mWeather.locationId);
            count = 0;
        });
        //弹出侧边菜单按钮
        navButton.setOnClickListener((view)-> drawerLayout.openDrawer(GravityCompat.START));
    }

    /**
     * 加载每日一图背景图片
     */
    private void loadBingPic() {
        HttpUtil.sendOkHttpRequest(API.BING_PIC_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String binPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",binPic);
                editor.apply();
                runOnUiThread(()-> Glide.with(WeatherActivity.this).load(binPic).into(bingPicImg));
            }
        });
    }

    /**
     * 根据locationId请求天气数据
     * @param locationId
     */
    private void requestWeather(final String locationId) {
       requestWeatherNow(locationId);
       requestWeather3Day(locationId);
       requestWeatherIndicesNow(locationId);
       requestAQI(locationId);
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
                showToast("获取空气质量数据失败");
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
                        runOnUiThread(()->showWeatherInfo());
                    }else{
                        showToast("获取空气质量数据失败");
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
                showToast("获取天气指数数据失败");
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
                        runOnUiThread(()->showWeatherInfo());
                    }else {
                        showToast("获取天气指数数据失败");
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
                showToast("获取未来三天天气数据失败");
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
                        runOnUiThread(()->showWeatherInfo());
                    }else {
                        showToast("获取未来三天天气数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestWeatherNow(String locationId) {
        String address = API.WEATHER_NOW_URL + "location=" + locationId + "&key=" + API.KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("获取天气数据失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    if (jsonObject.getInt("code")==200){
                        String nowText = jsonObject.getString("now");
                        Now now = new Gson().fromJson(nowText,Now.class);
                        mWeather.now = now;
                        count++;
                        runOnUiThread(()->showWeatherInfo());
                    }else {
                        showToast("获取天气数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 请求天气数据
     * @param countyName
     */
    public void requestLocationId(String countyName){
        mWeather.cityName = countyName;
        String address = API.LOCATION_ID_URL + "location=" + countyName + "&key=" + API.KEY;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject locationObject = new JSONObject(responseText);
                    if(locationObject.getInt("code")==200){
                        JSONArray jsonArray = locationObject.getJSONArray("location");
                        String locationId = jsonArray.getJSONObject(0).getString("id");
                        //保存当前地区的locationId
                        mWeather.locationId = locationId;
                        requestWeather(locationId);
                        //获取背景图片
                        loadBingPic();
                    }else{
                        showToast("获取locationId失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("获取locationId失败");
            }
        });
    }

    /**
     * 将天气数据保存到共享数据中
     */
    private void saveToSharedPreferences(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
        Gson gson = new Gson();
        String weather = gson.toJson(mWeather);
        editor.putString("weather",weather);
        editor.apply();
    }

    private void showWeatherInfo(){
        //count=4说明四个请求全部成功返回,count=-1说明已有缓存数据
        if (count == 4 || count == -1) {
            Log.d(TAG, "showWeatherInfo.............");
            if (count == 4){
                //将weather天气数据保存到共享参数中
                saveToSharedPreferences();
            }
            swipeRefresh.setRefreshing(false);
            String cityName = mWeather.cityName;
            String updateTime = mWeather.now.updateTime;
            //格式时间
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
            try {
                Date date = format1.parse(updateTime);
                updateTime = format2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String degree = mWeather.now.temperature + "℃";
            String weatherInfo = mWeather.now.info;
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);
            forecastLayout.removeAllViews();
            for (Forecasts.Forecast forecast : mWeather.forecasts.forecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                TextView dateText = view.findViewById(R.id.date_text);
                TextView infoText = view.findViewById(R.id.info_text);
                TextView maxText = view.findViewById(R.id.max_text);
                TextView minText = view.findViewById(R.id.min_text);
                dateText.setText(forecast.date);
                infoText.setText(forecast.info);
                maxText.setText(forecast.tempMax + "℃");
                minText.setText(forecast.tempMin + "℃");
                forecastLayout.addView(view);
            }
            if (mWeather.aqi != null) {
                aqiText.setText(mWeather.aqi.aqi);
                pm25Text.setText(mWeather.aqi.pm2p5);
            }
            Suggestions.Suggestion suggestion = null;
            suggestion = mWeather.suggestions.suggestionList.get(0);
            String carWash = suggestion.name + ": " + suggestion.text;

            suggestion = mWeather.suggestions.suggestionList.get(1);
            String sport = suggestion.name + ": " + suggestion.text;

            suggestion = mWeather.suggestions.suggestionList.get(2);
            String comfort = suggestion.name + ": " + suggestion.text;

            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);
            //将数据映射到视图上
            weatherLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String info){
        runOnUiThread(()-> {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
            //失败了才会弹Toast，同时关闭下拉刷新
            swipeRefresh.setRefreshing(false);
        });

    }
}