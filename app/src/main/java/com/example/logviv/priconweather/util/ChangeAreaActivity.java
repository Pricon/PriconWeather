package com.example.logviv.priconweather.util;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.logviv.priconweather.R;

/**
 * 更换地区
 */

public class ChangeAreaActivity extends Activity {
    private TextView areaText;
    private TextView tempText;
    private TextView weatherText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_area);

        areaText=(TextView)findViewById(R.id.area_text);
        tempText=(TextView)findViewById(R.id.temp_text);
        weatherText=(TextView)findViewById(R.id.weather_text);
    }
}
