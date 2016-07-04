package com.emin.digit.mobile.android.learning.practiceproject.common;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Samson on 16/6/22.
 */
public class ThisApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return mContext;
    }

    public static Resources getAppResources(){
        return getAppResources();
    }

}
