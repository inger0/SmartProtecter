package com.example.smartprotector.Config;

import android.app.Application;

import com.example.smartprotector.Bean.CurrentUser;

/**
 * Created by Huhu on 8/16/15.
 * 存放当前用户的属性
 */
public class MyApplication extends Application {
    private static MyApplication application = null;
    private CurrentUser currentUser;

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
