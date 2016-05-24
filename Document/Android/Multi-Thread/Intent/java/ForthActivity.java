package com.sokasyn.develop.learning.testintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by jim on 16/5/23.
 */
public class ForthActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forth);

        // 获取源界面传入的参数值
        String inputParam = this.getIntent().getStringExtra("inputParam");

        // 找到需要展示源界面传值的textView
        TextView textView = (TextView)findViewById(R.id.tw_showParamIn);
        textView.setText(inputParam);
    }
}
