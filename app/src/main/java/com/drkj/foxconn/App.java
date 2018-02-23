package com.drkj.foxconn;

import android.app.Application;

import com.drkj.foxconn.db.DbController;

/**
 * Created by ganlong on 2018/1/30.
 */

public class App extends Application {

    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DbController.getInstance();
    }
    public static App getInstance(){
        return instance;
    }
}
