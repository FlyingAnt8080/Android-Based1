package com.suse.litepaltest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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
        //进行任意一次数据库操作，BookStore.db数据库就会自动创建
        createDatabase.setOnClickListener((View view) -> Connector.getDatabase());
        //添加数据
        addData.setOnClickListener((View view)->{
            Book book = new Book();
            book.setName("The Da Vinci Code");
            book.setAuthor("Dan Brown");
            book.setPages(454);
            book.setPrice(16.96);
            book.setPress("Unknow");
            book.save();
            Toast.makeText(this, "add data succeeded", Toast.LENGTH_SHORT).show();
        });

        updateData.setOnClickListener((View view)->{
            //更新数据方法一
            /*
            Book book = new Book();
            book.setName("The Lost Symbol");
            book.setAuthor("Dan Brown");
            book.setPages(510);
            book.setPrice(19.95);
            book.setPress("Unknow");
            book.save();
            book.setPrice(10.99);
            book.save();*/

            //更新数据方法二
            Book book = new Book();
            book.setPrice(14.95);
            book.setPress("Anchor");
            book.updateAll("name = ? and author = ?","The Lost Symbol","Dan Brown");
        });

        //删除数据
        deleteData.setOnClickListener((View view)->
                LitePal.deleteAll(Book.class,"price < ?","15")
        );

        //查询数据
        selectData.setOnClickListener((View view)->{
            List<Book> books = LitePal.findAll(Book.class);
            for (Book book:books){
                Log.d(TAG, book.toString());
            }
        });
    }
}