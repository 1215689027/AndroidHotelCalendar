package com.customhotelcalendar.hotelcalendar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.customhotelcalendar.R;
/**
 * Created by Administrator on 2017/8/8.
 */

public class MenuActivity extends AppCompatActivity {

    private TextView peroidTv,singleTv;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_menu);
        peroidTv=(TextView)findViewById(R.id.peroid);
        singleTv=(TextView)findViewById(R.id.single);
        singleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(SingleSelectActivity.class);
            }
        });

        peroidTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(PeroidSelectedActivity.class);
            }
        });
    }

    private void gotoActivity(Class<?> target){
        Intent intent=new Intent(this,target);
        startActivity(intent);
    }
}
