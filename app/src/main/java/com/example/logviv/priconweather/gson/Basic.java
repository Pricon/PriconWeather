package com.example.logviv.priconweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 基本信息
 */
public class Basic {
    @SerializedName("city")
    public String cityName;  //城市名

    @SerializedName("id")
    public String weatherId;  //天气id

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime; //天气更新时间
    }
}
