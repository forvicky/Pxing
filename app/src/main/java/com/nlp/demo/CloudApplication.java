package com.nlp.demo;

import android.app.Application;

/**
 * Created by zdd on 2018/10/9
 */
public class CloudApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        if(true){
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }

    }
}
