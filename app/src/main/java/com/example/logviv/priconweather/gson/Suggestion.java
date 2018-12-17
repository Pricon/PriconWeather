package com.example.logviv.priconweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 出行建议
 */

public class Suggestion {
    @SerializedName("comf")  //舒适度
    public Comfort comfort;
    public class Comfort {
        @SerializedName("txt")
        public String info;
        @SerializedName("brf")
        public String comText;
    }

    @SerializedName("cw")
    public CarWash carWash;  //洗车
    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    @SerializedName("sport")
    public Sport sport;  //运动指数
    public class Sport {
        @SerializedName("txt")
        public String info;
    }


}
