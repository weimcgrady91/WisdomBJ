package com.qun.test.wisdombj.engine;

import android.util.Log;

import com.qun.test.wisdombj.ConstantValue;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2018/4/5.
 */

public class DataEngine {

    public interface OnParseDataListener {
        void onParseData(String result);
    }

    //    private Handler mHandler;
    private OnParseDataListener mOnParseDataListener;

    //    public DataEngine(Handler handler) {
//        mHandler = handler;
//    }
//

    public OnParseDataListener getOnParseDataListener() {
        return mOnParseDataListener;
    }

    public void setOnParseDataListener(OnParseDataListener onParseDataListener) {
        mOnParseDataListener = onParseDataListener;
    }

    private void parseData(final String result) {
        if (mOnParseDataListener != null) {
            mOnParseDataListener.onParseData(result);
        }
    }

    public void requestData(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                Log.e("weiqun12345", "result=" + result);
                parseData(result);
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("weiqun12345", " onError thread id=" + Thread.currentThread().getId());
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("weiqun12345", " onCancelled thread id=" + Thread.currentThread().getId());
            }

            @Override
            public void onFinished() {
                Log.e("weiqun12345", " onFinished thread id=" + Thread.currentThread().getId());
            }
        });
    }
}
