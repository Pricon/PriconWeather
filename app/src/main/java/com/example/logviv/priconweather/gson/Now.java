package com.example.logviv.priconweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 当前天气
 */

public class Now {
    @SerializedName("tmp")
    public String temperature; //当前温度

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;

    }
    @SerializedName("fl")
    public String fl;//体感温度
    @SerializedName("hum")
    public String hum;//相对湿度
    @SerializedName("pres")
    public String press;//大气压强

    @SerializedName("wind_dir")
    public String wind_dir;//风向
    @SerializedName("wind_sc")
    public String wind_sc;//风力
    @SerializedName("wind_spd")
    public String wind_spd;//风速

}
