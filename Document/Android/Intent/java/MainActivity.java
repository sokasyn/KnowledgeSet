package com.sokasyn.develop.learning.testintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定btnForth的点击事件
        Button btnToForth = (Button) findViewById(R.id.btnForth);
        addClickListener(btnToForth);
    }

    // 到第二个界面按钮点击事件
    public void btnToSndClicked(View view){
        toSecondActivity();
    }

    // 到第三个界面按钮点击事件,测试传值
    public void btnToThirdClicked(View view){
        toThirdActivity();
    }

    // 到第四个界面按钮点击
    // 通过按钮的点击监听,动态绑定按钮的事件
    private void addClickListener(Button button){
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("info","btnToForth clicked");
                toForthActivity();
            }
        });
    }


    // 跳转到第二个界面
    private void toSecondActivity(){
        String to = "com.sokasyn.develop.learning.testintent.SecondActivity";
        Intent intent = new Intent(to);
        startActivity(intent);
    }

    // 跳转到第三个界面
    private void toThirdActivity(){
        // 第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
        Intent intent = new Intent(MainActivity.this,ThirdActivity.class);
        // 用Budle携带数据
        Bundle bundle = new Bundle();
        bundle.putString("userName","Samson");
        bundle.putString("password","1234");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // 跳转到第四个界面
    private void toForthActivity(){
        // 获取用户输入的值
        EditText editText = (EditText)findViewById(R.id.editText);
        String inputText = editText.getText().toString();

        // 直接将该值配置到Intent中
        Intent intent = new Intent(MainActivity.this , ForthActivity.class);
        intent.putExtra("inputParam" ,inputText);
        startActivity(intent);
    }

    private void printMessage(String msg){
        System.out.println(msg);
    }
}
