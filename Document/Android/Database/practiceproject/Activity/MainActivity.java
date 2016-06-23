package com.emin.digit.mobile.android.learning.practiceproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate");
        configButtonListener();
    }

    private void configButtonListener(){
        Button btnNum1 = (Button)findViewById(R.id.idBtnNum1);
        btnNum1.setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.idBtnNum1:{
                    Log.i(TAG,"Button number1 clicked...");
                    toTest1();
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

    // Button Events
    private void toTest1(){
        Log.i(TAG,"toTest1");
        Intent intent = new Intent();
        intent.setAction("com.emin.intent.action.number1");
        startActivity(intent);
    }
}
