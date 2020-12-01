package com.suse.databasetest;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MyDatabaseHelper dbHelper;
    private Button createDatabase;
    private Button addData;
    private Button updateData;
    private Button deleteData;
    private Button selectData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase = findViewById(R.id.create_database);
        addData = findViewById(R.id.add_data);
        updateData = findViewById(R.id.update_data);
        deleteData = findViewById(R.id.delete_data);
        selectData = findViewById(R.id.select_data);

        /**
         * 参数1：Context
         * 参数2：数据库名
         * 参数3：查询数据库时返回的自定义的Cursor
         * 参数4：数据库版本
         */
        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,2);
        //创建数据库
        createDatabase.setOnClickListener((View view)->{
            dbHelper.getWritableDatabase();
        });

        //添加数据
        addData.setOnClickListener((View view)->{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //开始组装第一条数据
            values.put("name","The Da Vinci Code");
            values.put("author","Dan Brown");
            values.put("pages",454);
            values.put("price",16.96);
            db.insert("Book",null,values);//插入第一条数据

            values.put("name","The Lost Symbol");
            values.put("author","Dan Brown");
            values.put("pages",510);
            values.put("price",19.95);
            db.insert("Book",null,values);
            Toast.makeText(this, "add data succeeded", Toast.LENGTH_SHORT).show();
        });

        //修改数据
        updateData.setOnClickListener((View view)->{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("price",10.99);
            db.update("Book",values,"name = ?",new String[]{"The Da Vinci Code"});
            Toast.makeText(this, "update data succeeded", Toast.LENGTH_SHORT).show();
        });

        //删除数据
        deleteData.setOnClickListener((View view)->{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int res = db.delete("Book","pages > ?",new String[]{"500"});
            if (res>0){
                Toast.makeText(this, "delete data succeeded", Toast.LENGTH_SHORT).show();
            }
        });

        //查询数据
        selectData.setOnClickListener((View view)->{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //查询Book表中所有的数据
            Cursor cursor = db.query("Book",null,null,null,null,null,null);
            Log.d(TAG, "name\tauthor\tpages\tprice");
            if(cursor.moveToFirst()){
                do{
                    //遍历Cursor对象，取出数据并打印
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                    double price = cursor.getDouble(cursor.getColumnIndex("price"));
                    Log.d(TAG, name + "\t" + author + "\t" + pages + "\t" + price);
                }while (cursor.moveToNext());
            }
            cursor.close();
        });
    }
}