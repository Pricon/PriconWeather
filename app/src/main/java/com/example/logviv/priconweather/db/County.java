package com.example.logviv.priconweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by logviv on 2018/12/2.
 * 县
 */

public class County extends DataSupport {
    private int id;    //数据表中的id
    private String countyName;   //县名
    private int cityId;        //所属市的id
    private String weatherId;     //天气id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
