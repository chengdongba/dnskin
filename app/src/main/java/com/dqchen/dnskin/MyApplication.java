package com.dqchen.dnskin;

import android.app.Application;

import com.dqchen.skin_core.SkinManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
