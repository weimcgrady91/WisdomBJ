package com.qun.test.wisdombj.engine;

import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2018/4/5.
 */

public class DataEngine {

    private OnRequestDataListener mOnRequestDataListener;

    public void setOnRequestDataListener(OnRequestDataListener onRequestDataListener) {
        mOnRequestDataListener = onRequestDataListener;
    }


    public void requestData(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("weiqun12345", "request Data success=" + result);
                if (mOnRequestDataListener != null) {
                    mOnRequestDataListener.onProcessData(result);
                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("weiqun12345", " onError thread id=" + Thread.currentThread().getId());
                if (mOnRequestDataListener != null) {
                    mOnRequestDataListener.onRequestDataError();
                }
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("weiqun12345", " onCancelled thread id=" + Thread.currentThread().getId());
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public interface OnRequestDataListener {
        void onProcessData(String result);

        void onRequestDataError();
    }
}
