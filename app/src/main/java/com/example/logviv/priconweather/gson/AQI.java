package com.example.logviv.priconweather.gson;

/**
 * Created by logviv on 2018/12/10.
 */

public class AQI {
    public AQICity city;
    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
