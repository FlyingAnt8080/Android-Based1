package com.suse.activitytest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button button1 = findViewById(R.id.button_1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(FirstActivity.this, "you clicked button1", Toast.LENGTH_SHORT).show();
                //显示Intent跳转
                //Intent intent = new Intent(FirstActivity.this,SecondActivity.class);
                //startActivity(intent);

                //隐式Intent跳转
                /*Intent intent = new Intent("com.suse.activitytest.ACTION_START");
                intent.addCategory("com.suse.activitytest.MY_CATEGORY");
                startActivity(intent);*/

                //其他隐式Intent跳转

                //通过Intent跳转到浏览器
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http:www.baidu.com"));
                startActivity(intent);*/

                //通过Intent跳转到拨号盘
               /* Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);*/

                //通过Intent传递数据
                /*String data = "Hello SecondActivity";
                Intent intent = new Intent(FirstActivity.this,SecondActivity.class);
                intent.putExtra("extra_data",data);
                startActivity(intent);*/

                //返回数据给上一个活动(通过startActivityForResult启动其他Activity，当跳转的Activity销毁时，可以向本Activity传递数据)
               /* Intent intent = new Intent(FirstActivity.this,SecondActivity.class);
                startActivityForResult(intent,1);*/

                //启动活动的最佳写法
                SecondActivity.actionStart(FirstActivity.this,"data1","data2");
            }
        });
    }

    //跳转到其他Activity，返回到当前Activity的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String returnedData = data.getStringExtra("data_return");
                    Log.d("FirstActivity", returnedData);
                }
            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //用于创建选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    //选项菜单选择器
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(this, "You Clicked Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reomve_item:
                Toast.makeText(this, "You Clicked Remove", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}