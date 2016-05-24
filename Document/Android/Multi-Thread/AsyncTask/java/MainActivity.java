package com.sokasyn.develop.learning.TestMultiTasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configBtnClickListener();
    }

    // 配置按钮的点击事件
    private void configBtnClickListener(){
        // 配置AsyncTask测试按钮点击事件
        Button btnAsyncTask = (Button)findViewById(R.id.idBtnAsyncTask);
        btnAsyncTask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toAsyncTaskTest();
            }
        });

        // 配置AsyncTask测试按钮点击事件
        Button btnHandler = (Button)findViewById(R.id.idBtnHandler);
        btnHandler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toHandlerTest();
            }
        });
    }


    // 跳转到AsyncTask测试界面
    private void toAsyncTaskTest(){
        Intent intent = new Intent(MainActivity.this , AsyncTaskActivity.class);
        startActivity(intent);
    }

    // 跳转到Handler测试界面
    private void toHandlerTest(){
        Intent intent = new Intent(MainActivity.this , HandlerActivity.class);
        startActivity(intent);
    }
}
