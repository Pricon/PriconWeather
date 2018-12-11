package com.example.logviv.priconweather;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * 设置界面
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView about;
    private Button ic_rigth;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        about = (TextView) findViewById(R.id.about);
        ic_rigth=(Button)findViewById(R.id.right);
        back = (Button) findViewById(R.id.back_button);
        //about.getBackground().setAlpha(12);
        about.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==about||v==ic_rigth){
            startActivity(new Intent(SettingActivity.this,AboutActivity.class));
        }
        else if (v==back){
            finish();
        }
    }
}
