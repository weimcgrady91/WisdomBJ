package com.qun.test.wisdombj;

import android.app.Application;
import android.content.Context;

import org.xutils.x;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class App extends Application {
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        sContext = getApplicationContext();
    }

}
