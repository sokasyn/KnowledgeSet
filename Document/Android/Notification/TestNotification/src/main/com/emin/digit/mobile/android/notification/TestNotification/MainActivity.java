package com.emin.digit.mobile.android.notification.TestNotification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // log
    private static final String TAG = MainActivity.class.getSimpleName();

    private String mDeviceID;

    private Button btnStart;
    private Button btnStop;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前布局视图
        setContentView(R.layout.activity_main);

        // MQTT推送测试
        testMQTTNotification();

        // 本地通知测试
//        testLocalNotification();
    }

    // MQTT(服务器端)推送
    private void testMQTTNotification(){
        mDeviceID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        Log.i(TAG,"mDeviceID:" + mDeviceID); // ae24197dc8b7d9b0
        TextView targetText = (TextView)findViewById(R.id.target_text);
        targetText.setText(mDeviceID);

        // 开始推送服务按钮
        btnStart = (Button)findViewById(R.id.start_button);
        btnStart.setOnClickListener(mClickListener);

        // 停止推送服务按钮
        btnStop  = (Button)findViewById(R.id.stop_button);
        btnStop.setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.start_button:{
                    Editor editor = getSharedPreferences(PushServiceMqtt.TAG,MODE_PRIVATE).edit();
                    editor.putString(PushServiceMqtt.PREF_DEVICE_ID,mDeviceID);
                    editor.commit();
                    PushServiceMqtt.actionStart(getApplicationContext());
                    btnStart.setEnabled(false);
                    btnStop.setEnabled(true);
                    break;
                }
                case R.id.stop_button:{
                    PushServiceMqtt.actionStop(getApplicationContext());
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG,"onResume");
        SharedPreferences p = getSharedPreferences(PushServiceMqtt.TAG, MODE_PRIVATE);
        boolean started = p.getBoolean(PushServiceMqtt.PREF_STARTED, false);

        ((Button) findViewById(R.id.start_button)).setEnabled(!started);
        ((Button) findViewById(R.id.stop_button)).setEnabled(started);
    }

    /*
    // 本地推送
    private void testLocalNotification(){
        // 实例化Button
        Button btn = (Button)findViewById(R.id.Button1);
        // 添加事件监听器
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 实例化Intent
                Intent intent = new Intent();
                // 设置Intent action属性
                String action = "com.android.notification.MY_ACTION"; // Broadcast Receiver action
                intent.setAction(action);
                // 发起广播
                sendBroadcast(intent);
            }
        });
    }
    */





    // 创建事件监听器
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
}
