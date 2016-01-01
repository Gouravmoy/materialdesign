package com.example.lenovo.materialdesign;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 12/30/2015.
 */
public class MyApplication extends Application {
    public static final String API_KEY_ROTTEN_TOMATOES = "ny97sdcpqetasj8a4v2na8va";
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
