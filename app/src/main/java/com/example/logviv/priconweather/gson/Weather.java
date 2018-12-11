package com.example.logviv.priconweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 一个实例类，用来引用各个实体类
 */

public class Weather {
    public String status; //成功则返回ok

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
