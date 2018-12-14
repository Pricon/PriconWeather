package com.example.logviv.priconweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 搜索地区
 */

public class AddAreaActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText inputArea;
    private Button search;
    private Button beijing;
    private Button shanghai;
    private Button guangzhou;
    private Button shenzhen;
    private Button hangzhou;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_main);

        inputArea=(EditText)findViewById(R.id.input_city_name);
        back=(Button)findViewById(R.id.back_button);
        search=(Button)findViewById(R.id.search_button);
        beijing=(Button)findViewById(R.id.beijing);
        shanghai=(Button)findViewById(R.id.shanghai);

        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }
    }
}
