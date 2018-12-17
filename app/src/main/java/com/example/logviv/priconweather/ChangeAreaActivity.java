package com.example.logviv.priconweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.logviv.priconweather.db.City;
import com.example.logviv.priconweather.db.County;
import com.example.logviv.priconweather.db.Province;
import com.example.logviv.priconweather.gson.Basic;
import com.example.logviv.priconweather.gson.Weather;
import com.example.logviv.priconweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 更换地区
 */

public class ChangeAreaActivity extends Activity implements View.OnClickListener{
    private ListView listView;
    private Button add_city;
    private Button back;
    private ArrayAdapter<String> adapter; //适配器
    private List<String> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_area);

        listView=(ListView)findViewById(R.id.listview);
        add_city=(Button)findViewById(R.id.add_city);
        back=(Button)findViewById(R.id.back_button);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        back.setOnClickListener(this);
        add_city.setOnClickListener(this);
        showInfo();

        //listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void showInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        Weather weather = Utility.handleWeatherResponse(weatherString);
        String cityName=weather.basic.cityName;
        String temperature=weather.now.temperature;
        String weatherInfo=weather.now.more.info;
        if (cityName!=null) { //将数据显示在界面上
            dataList.add(cityName);
            dataList.add(temperature);
            dataList.add(weatherInfo);
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        }

    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==add_city){
            startActivity(new Intent(ChangeAreaActivity.this,AddAreaActivity.class));
        }
    }
}
