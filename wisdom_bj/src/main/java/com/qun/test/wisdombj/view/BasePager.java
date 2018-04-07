package com.qun.test.wisdombj.view;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2018/4/6.
 */

public abstract class BasePager {
    public View mRootView;
    public Context mContext;

    public BasePager(Context context) {
        mContext = context;
        mRootView = initView();
    }

    public abstract void loadData();

    public abstract View initView();
}
