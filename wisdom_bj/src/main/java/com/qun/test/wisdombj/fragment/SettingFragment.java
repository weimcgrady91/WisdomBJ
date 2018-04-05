package com.qun.test.wisdombj.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qun.test.wisdombj.R;

/**
 * Created by Administrator on 2018/4/5.
 */

public class SettingFragment extends BasePageFragment {
    public SettingFragment() {

    }

    public static SettingFragment newInstance() {
        Bundle bundle = new Bundle();
        SettingFragment settingFragment = new SettingFragment();
        settingFragment.setArguments(bundle);
        return settingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null, false);
        return view;
    }

    @Override
    public void loadData() {
        Log.e("weiqun12345","SettingFragment initData");
    }
}
