package com.suse.filepersistencetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.edit);
        String inputText = load();
        if(!TextUtils.isEmpty(inputText)){
            mEditText.setText(inputText);
            //将光标移动到文本末尾
            mEditText.setSelection(inputText.length());
            Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = mEditText.getText().toString();
        save(inputText);
    }

    //从文件data中读取数据
    private String load(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
           try {
               in = openFileInput("data");
               reader = new BufferedReader(new InputStreamReader(in));
               String line;
               while((line=reader.readLine()) != null){
                   content.append(line);
               }
           }finally {
               reader.close();
           }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    //将数据存储到文件data中
    private void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
           try {
               out = openFileOutput("data", Context.MODE_APPEND);
               writer = new BufferedWriter(new OutputStreamWriter(out));
               writer.write(inputText);
           }finally {
               if (writer!=null){
                   writer.close();
               }
           }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}