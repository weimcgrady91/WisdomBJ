package com.qun.test.wisdombj.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qun.test.wisdombj.R;

/**
 * Created by Administrator on 2018/4/5.
 */

public class GovernmentFragment extends BaseFragment {
    public GovernmentFragment() {

    }

    public static GovernmentFragment newInstance() {
        Bundle bundle = new Bundle();
        GovernmentFragment governmentFragment = new GovernmentFragment();
        governmentFragment.setArguments(bundle);
        return governmentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_government, null, false);
        return view;
    }

    @Override
    public void loadData() {
        
    }
}
