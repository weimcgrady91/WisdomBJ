package com.qun.test.wisdombj.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.qun.test.wisdombj.R;

/**
 * Created by Administrator on 2018/4/6.
 */

public class SpecialPager extends BasePager {
    public SpecialPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pager_special_detail, null, false);
        return view;
    }

    @Override
    public void loadData() {

    }
}
