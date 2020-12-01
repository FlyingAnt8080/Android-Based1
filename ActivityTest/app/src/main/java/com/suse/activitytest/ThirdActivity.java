package com.suse.activitytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_layout);
        Button button3 = findViewById(R.id.button_3);
        button3.setOnClickListener((View v)->{
            ActivityCollector.finishAll();
        });
    }
}