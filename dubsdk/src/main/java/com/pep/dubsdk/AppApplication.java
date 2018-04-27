package com.pep.dubsdk;

import android.app.Application;

public class AppApplication extends Application {
    private static AppApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


    public static AppApplication getInstance() {
        return instance;
    }
}
