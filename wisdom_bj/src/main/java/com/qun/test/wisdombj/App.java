package com.qun.test.wisdombj;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class App extends Application {
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

}
