package com.example.logviv.priconweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 未来几天的天气信息
 */

public class Forecast {
    public String date; //预报日期

    @SerializedName("tmp")
    public Temperature temperature;  //温度

    @SerializedName("cond")
    public More more;  //天气状况

    public class Temperature {
        public String max;
        public String min;

    }
    public class More {
        @SerializedName("txt_d") //白天天气状况
        public String info;

    }
}
