package com.example.logviv.priconweather.gson;

/**
 * 当前空气质量的情况
 */

public class AQI {
    public AQICity city;
    public class AQICity {
        public String qlty; //空气质量
        public String main; //主要污染物
        public String aqi; //AQI指数
        public String pm10; //pm10指数
        public String pm25;  //PM2.5指数
        public String no2; //二氧化氮
        public String so2; //二氧化硫
    }
}
