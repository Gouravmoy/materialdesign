package com.example.lenovo.materialdesign;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 12/30/2015.
 */
public class MyApplication extends Application {
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
