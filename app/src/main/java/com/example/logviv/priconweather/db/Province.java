package com.example.logviv.priconweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by logviv on 2018/12/2.
 * 省
 */

public class Province extends DataSupport{
    private int id;   //各个省在数据表中的id
    private String provinceName;  //省名
    private int provinceCode;   //各个省的代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

}
