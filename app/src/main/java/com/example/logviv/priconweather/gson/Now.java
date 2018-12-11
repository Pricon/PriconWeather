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
}
