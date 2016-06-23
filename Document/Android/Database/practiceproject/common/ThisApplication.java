package com.emin.digit.mobile.android.common;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Samson on 16/6/22.
 */
public class ThisApplication extends Application{

    private static ThisApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext(){
        return mContext;
    }

    public static Resources getAppResources(){
        return getAppResources();
    }

}
