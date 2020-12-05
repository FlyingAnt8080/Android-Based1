package com.suse.androidthreadtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int UPDATE_TEXT = 1;
    private Button changeTextBtn;
    private TextView text;
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    //在这里可以进行UI操作
                    text.setText("Nice to meet you");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeTextBtn = findViewById(R.id.change_text);
        text = findViewById(R.id.text);
        changeTextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_text:
               new Thread(()->{
                   Message message = new Message();
                   message.what = UPDATE_TEXT;
                   handler.sendMessage(message);
               }).start();
                break;
            default:
                break;
        }
    }
}