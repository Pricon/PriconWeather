package com.example.logviv.priconweather.gson;

/**
 * Created by logviv on 2018/12/10.
 */

public class AQI { //当前空气质量的情况
    public AQICity city;
    public class AQICity {
        public String aqi; //AQI指数
        public String pm25;  //PM2.5指数
    }
}
