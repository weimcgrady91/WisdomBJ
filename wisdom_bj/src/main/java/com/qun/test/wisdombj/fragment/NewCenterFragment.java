package com.qun.test.wisdombj.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.qun.test.wisdombj.ConstantValue;
import com.qun.test.wisdombj.OnFillMenuDataListener;
import com.qun.test.wisdombj.R;
import com.qun.test.wisdombj.bean.NewsMenu;
import com.qun.test.wisdombj.engine.DataEngine;

/**
 * Created by Administrator on 2018/4/5.
 */

public class NewCenterFragment extends BasePageFragment {


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                NewsMenu newsMenu = (NewsMenu) msg.obj;
                Log.e("weiqun12345", newsMenu.toString());
                mOnFillMenuDataListener.onFillMenu(newsMenu.getData());
            }
        }
    };

    public NewCenterFragment() {

    }

    private OnFillMenuDataListener mOnFillMenuDataListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFillMenuDataListener) {
            mOnFillMenuDataListener = (OnFillMenuDataListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement mOnFillMenuDataListener");
        }
    }

    public static NewCenterFragment newInstance() {
        Bundle bundle = new Bundle();
        NewCenterFragment newCenterFragment = new NewCenterFragment();
        newCenterFragment.setArguments(bundle);
        return newCenterFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_center, null, false);
        return view;
    }


    @Override
    public void loadData() {
        Log.e("weiqun12345", "NewCenterFragment initData");
        DataEngine dataEngine = new DataEngine();
        dataEngine.setOnParseDataListener(new DataEngine.OnParseDataListener() {
            @Override
            public void onParseData(final String result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        NewsMenu newsMenu = gson.fromJson(result, NewsMenu.class);
                        if (mHandler != null) {
                            Message message = mHandler.obtainMessage();
                            message.what = 1;
                            message.obj = newsMenu;
                            mHandler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });
        dataEngine.requestData(ConstantValue.URL_HOST + ConstantValue.NEWS_CATEGORIES);

    }
}
