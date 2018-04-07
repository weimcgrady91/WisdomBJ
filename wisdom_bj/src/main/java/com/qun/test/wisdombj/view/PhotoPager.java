package com.qun.test.wisdombj.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qun.test.wisdombj.ConstantValue;
import com.qun.test.wisdombj.R;
import com.qun.test.wisdombj.bean.NewsDetail;
import com.qun.test.wisdombj.bean.PhotosBean;
import com.qun.test.wisdombj.engine.DataEngine;
import com.qun.test.wisdombj.util.SPUtil;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/6.
 */

public class PhotoPager extends BasePager {
    private final static int WHAT_FETCH_DATA_FROM_SERVER = 1;
    private final static int WHAT_FETCH_DATA_FROM_SERVER_ERR = 2;
    private String mPhotoUrl;
    private ArrayList<PhotosBean.PhotoNews> mPhotoNews;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_FETCH_DATA_FROM_SERVER:
                    PhotosBean photosBean = (PhotosBean) msg.obj;
                    mPhotoNews.clear();
                    mPhotoNews.addAll(photosBean.data.news);
                    mAdapter.notifyDataSetChanged();
                    break;
                case WHAT_FETCH_DATA_FROM_SERVER_ERR:

                    break;
            }

        }
    };
    private PhotoAdapter mAdapter;
    private GridView mGridLayout;
    private ListView mLv;

    public PhotoPager(Context context, String url) {
        super(context);
        mPhotoUrl = url;
    }

    @Override
    public View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pager_photo_detail, null, false);
        mLv = view.findViewById(R.id.lv);
        mGridLayout = view.findViewById(R.id.gv);
        mPhotoNews = new ArrayList<>();
        mAdapter = new PhotoAdapter();
        mLv.setAdapter(mAdapter);
        mGridLayout.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void loadData() {
        DataEngine dataEngine = new DataEngine();
        dataEngine.setOnRequestDataListener(new DataEngine.OnRequestDataListener() {
            @Override
            public void onRequestDataError() {
                mHandler.sendEmptyMessage(WHAT_FETCH_DATA_FROM_SERVER_ERR);
            }

            @Override
            public void onProcessData(final String result) {
                new Thread(new ProcessDataRunnable(WHAT_FETCH_DATA_FROM_SERVER, mPhotoUrl, result, mHandler)).start();
            }
        });
        dataEngine.requestData(ConstantValue.URL_HOST + mPhotoUrl);
    }

    private class ProcessDataRunnable implements Runnable {
        private String result;
        private String url;
        private Handler handler;
        private int what;

        public ProcessDataRunnable(int what, String url, String result, Handler handler) {
            this.url = url;
            this.result = result;
            this.handler = handler;
            this.what = what;
        }

        @Override
        public void run() {
            if (!TextUtils.isEmpty(url)) {
                SPUtil.putData(url, result);
            }
            Gson gson = new Gson();
            PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);
            sendMessage(photosBean, what);
        }

        private void sendMessage(PhotosBean photosBean, int what) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.what = what;
                message.obj = photosBean;
                handler.sendMessage(message);
            }
        }
    }

    private class PhotoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mPhotoNews.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhotoNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_photos_news, null, false);
                viewHolder = new ViewHolder();
                viewHolder.ivPic = convertView.findViewById(R.id.iv_pic);
                viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            x.image().bind(viewHolder.ivPic, mPhotoNews.get(position).listimage);
            viewHolder.tvTitle.setText(mPhotoNews.get(position).title);
            return convertView;
        }
    }

    private class ViewHolder {
        ImageView ivPic;
        TextView tvTitle;
    }
}
