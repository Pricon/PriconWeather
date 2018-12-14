package com.example.logviv.priconweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.logviv.priconweather.gson.Forecast;
import com.example.logviv.priconweather.gson.Weather;
import com.example.logviv.priconweather.service.AutoUpdateService;
import com.example.logviv.priconweather.util.HttpUtil;
import com.example.logviv.priconweather.util.Utility;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherActivity extends Activity {
    private ScrollView weatherLayout; //布局
    private TextView titleCity;  //显示选中的地区
    private TextView titleUpdateTime;  //更新的时间
    private TextView degreeText;  //显示温度
    private TextView weatherInfoText;  //天气信息
    private LinearLayout forecastLayout;  //未来几天天气的信息布局
    private TextView qualityText; //空气质量，取值范围：优、良轻度污染等等
    private TextView mainText; //主要污染物
    private TextView aqiText;  //显示空气质量信息
    private TextView pm10Text; //pm10指数
    private TextView pm25Text; //pm2.5指数
    private TextView no2Text; //二氧化氮
    private TextView so2Text; //二氧化硫
    private TextView comfortText;  //舒适度信息
    private TextView carWashText;  //洗车信息
    private TextView sportText;   //运动信息
    public SwipeRefreshLayout swipeRefresh;  //下拉刷新
    private String mWeatherId;
    public DrawerLayout drawerLayout;
    private Button navButton;  //选择地区
    private Button setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //根据SDK的版本选择不同的显示方式
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        qualityText = (TextView) findViewById(R.id.quality);
//        mainText = (TextView) findViewById(R.id.main_text);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
//        pm10Text = (TextView) findViewById(R.id.pm10_text);
//        no2Text = (TextView) findViewById(R.id.no2_text);
//        so2Text = (TextView) findViewById(R.id.so2_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        setting=(Button)findViewById(R.id.setting_button);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
                Toast.makeText(WeatherActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
            }
        });
        //选择地区按钮点击事件
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.openDrawer(GravityCompat.START);
                startActivity(new Intent(WeatherActivity.this,ChangeAreaActivity.class));
            }
        });
        //跳转到关于界面
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WeatherActivity.this,SettingActivity.class));
            }
        });

    }

    /**
     * 根据天气id 请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=b1ebd5b96e8340b48a9b735b464fde31";
        //向服务器发送请求
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        //loadBingPic();
    }

    /**
     * 处理并展示Weather 实体类中的数据
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        //温度显示为摄氏温度，可在此修改添加华氏温度？
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        //用for循环来显示未来几天的天气
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text); //时间
            TextView infoText = (TextView) view.findViewById(R.id.info_text);  //天气
            TextView maxText = (TextView) view.findViewById(R.id.max_text);  //最高温度
            TextView minText = (TextView) view.findViewById(R.id.min_text);  //最低温度
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max+"℃");
            minText.setText("/"+forecast.temperature.min+"℃");
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            qualityText.setText(weather.aqi.city.qlty);
            aqiText.setText("AQI指数："+weather.aqi.city.aqi);
            pm25Text.setText("pm2.5指数："+weather.aqi.city.pm25);
//            mainText.setText(weather.aqi.city.main);
//            pm10Text.setText(weather.aqi.city.pm10);
//            no2Text.setText(weather.aqi.city.no2);
//            so2Text.setText(weather.aqi.city.so2);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        //每3小时自动更新天气
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

}