package com.suse.providertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String newId;
    private Button addData;
    private Button queryData;
    private Button updateData;
    private Button deleteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addData = findViewById(R.id.add_data);
        queryData = findViewById(R.id.query_data);
        updateData = findViewById(R.id.update_data);
        deleteData = findViewById(R.id.delete_data);

        addData.setOnClickListener((View view)->{
            //添加数据
            Uri uri = Uri.parse("content://com.suse.databasetest.provider/book");
            ContentValues values = new ContentValues();
            values.put("name","A Clash of Kings");
            values.put("author","George Martin");
            values.put("pages",1040);
            values.put("price",22.85);
            Uri newUri = getContentResolver().insert(uri,values);
            newId = newUri.getPathSegments().get(1);
        });

        queryData.setOnClickListener((View view)->{
            //查询数据
            Uri uri = Uri.parse("content://com.suse.databasetest.provider/book");
            Cursor cursor = getContentResolver().query(uri,null,null,null,
                    null);
            if (cursor != null){
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                    double price = cursor.getDouble(cursor.getColumnIndex("price"));
                    Log.d(TAG, "book name is " + name);
                    Log.d(TAG, "book author is " + author);
                    Log.d(TAG, "book pages is " + pages);
                    Log.d(TAG, "book price is " + price);
                }
                cursor.close();
            }
        });

        updateData.setOnClickListener((View view)->{
            //更新数据
            Uri uri = Uri.parse("content://com.suse.databasetest.provider/book/"+newId);
            ContentValues values = new ContentValues();
            values.put("name","A Storm Of Swords");
            values.put("pages",1216);
            values.put("price",24.05);
            getContentResolver().update(uri,values,null,null);
        });

        deleteData.setOnClickListener((View view)->{
            //删除数据
            Uri uri = Uri.parse("content://com.suse.databasetest.provider/book/"+newId);
            getContentResolver().delete(uri,null,null);
        });
    }
}