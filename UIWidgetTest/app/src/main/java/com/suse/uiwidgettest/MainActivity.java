package com.suse.uiwidgettest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 *通过实现View.OnClickListener接口来实现点击事件
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mEditText;
    private Button mButton;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mEditText = findViewById(R.id.edit_text);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                //String inputText = mEditText.getText().toString();
                //Toast.makeText(this, inputText, Toast.LENGTH_SHORT).show();
                //设置图片
               // mImageView.setImageResource(R.drawable.img_2);

                //设置显示或隐藏
                /*if(mProgressBar.getVisibility()==View.GONE){
                    mProgressBar.setVisibility(View.VISIBLE);
                }else {
                    mProgressBar.setVisibility(View.GONE);
                }*/

                //设置进度条变化
                /*int progress = mProgressBar.getProgress();
                progress += 10;
                mProgressBar.setProgress(progress);*/

                //设置警告对话框
                /*AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("This is Dialog");
                dialog.setMessage("Something important");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();*/

                //设置进度条对话框
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("This is ProgressDialog");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                break;
            default:
        }
    }
}