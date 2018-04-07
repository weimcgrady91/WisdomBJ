package com.qun.test.wisdombj.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.qun.test.wisdombj.ConstantValue;
import com.qun.test.wisdombj.OnFillMenuDataListener;
import com.qun.test.wisdombj.R;
import com.qun.test.wisdombj.bean.NewsMenu;
import com.qun.test.wisdombj.bean.NewsMenuData;
import com.qun.test.wisdombj.engine.DataEngine;
import com.qun.test.wisdombj.util.SPUtil;
import com.qun.test.wisdombj.view.BasePager;
import com.qun.test.wisdombj.view.InteractionPager;
import com.qun.test.wisdombj.view.NewsPager;
import com.qun.test.wisdombj.view.NoScrollViewPager;
import com.qun.test.wisdombj.view.PhotoPager;
import com.qun.test.wisdombj.view.SpecialPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/5.
 */

public class NewCenterFragment extends BaseFragment {

    private NewsMenu mNewsMenu;
    private List<BasePager> mDetails = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mNewsMenu = (NewsMenu) msg.obj;
                List<NewsMenuData> details = mNewsMenu.getData();
                for (int i = 0; i < details.size(); i++) {
                    int type = details.get(i).getType();
                    switch (type) {
                        case 1:
                            mDetails.add(new NewsPager(getActivity(), details.get(i).getChildren()));
                            break;
                        case 10:
                            mDetails.add(new SpecialPager(getActivity()));
                            break;
                        case 2:
                            mDetails.add(new PhotoPager(getActivity(),details.get(i).getUrl()));
                            break;
                        case 3:
                            mDetails.add(new InteractionPager(getActivity()));
                            break;
                    }
                }
                mAdapter.notifyDataSetChanged();
                mOnFillMenuDataListener.onFillMenu(mNewsMenu.getData());
            }
        }
    };
    private DetailsPagerAdapter mAdapter;

    public void setDetailPage(int position) {
        mViewPager.setCurrentItem(position, false);
    }

    private NoScrollViewPager mViewPager;

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
        mViewPager = view.findViewById(R.id.vp);
        mAdapter = new DetailsPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void loadData() {
        String cache = (String) SPUtil.getData(ConstantValue.URL_HOST + ConstantValue.NEWS_CATEGORIES, "");
        if (TextUtils.isEmpty(cache)) {
            fetchDataFromServer(false);
        } else {
            fetchDataFromCache(cache);
        }
    }

    private void fetchDataFromCache(final String cache) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                NewsMenu newsMenu = gson.fromJson(cache, NewsMenu.class);
                if (mHandler != null) {
                    Message message = mHandler.obtainMessage();
                    message.what = 1;
                    message.obj = newsMenu;
                    mHandler.sendMessage(message);
                }
                fetchDataFromServer(true);
            }
        }).start();
    }

    private void fetchDataFromServer(final boolean hasCache) {
        DataEngine dataEngine = new DataEngine();
        dataEngine.setOnRequestDataListener(new DataEngine.OnRequestDataListener() {
            @Override
            public void onRequestDataError() {

            }

            @Override
            public void onProcessData(final String result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SPUtil.putData(ConstantValue.URL_HOST + ConstantValue.NEWS_CATEGORIES, result);
                        if (!hasCache) {
                            Gson gson = new Gson();
                            NewsMenu newsMenu = gson.fromJson(result, NewsMenu.class);
                            if (mHandler != null) {
                                Message message = mHandler.obtainMessage();
                                message.what = 1;
                                message.obj = newsMenu;
                                mHandler.sendMessage(message);

                            }
                        }

                    }
                }).start();
            }
        });
        dataEngine.requestData(ConstantValue.URL_HOST + ConstantValue.NEWS_CATEGORIES);
    }


    private class DetailsPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mDetails.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager detail = mDetails.get(position);
            View view = detail.mRootView;
            detail.loadData();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
