package com.sokasyn.develop.learning.testintent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by jim on 16/5/23.
 */
public class ThirdActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // 如果源activity是用Bundle携带数据的,则用Bundle来接收数据
        Bundle bundle = this.getIntent().getExtras();
        String userName = bundle.getString("userName");
        String password = bundle.getString("password");
        Log.i("userName:" , userName);
        Log.w("password:" , password);
    }
}
