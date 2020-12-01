package com.suse.sharedpreferencestest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button saveData = findViewById(R.id.save_data);
        Button restoreData = findViewById(R.id.restore_data);

        saveData.setOnClickListener((View view)->{
            SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
            editor.putString("name","Tom");
            editor.putInt("age",28);
            editor.putBoolean("married",false);
            editor.apply();
        });

        restoreData.setOnClickListener((View view)->{
            SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
            String name = pref.getString("name","");
            int age = pref.getInt("age",0);
            boolean married = pref.getBoolean("married",false);
            Log.d(TAG, "name is " + name);
            Log.d(TAG, "age is " + age);
            Log.d(TAG, "married is " + married);
        });
    }
}