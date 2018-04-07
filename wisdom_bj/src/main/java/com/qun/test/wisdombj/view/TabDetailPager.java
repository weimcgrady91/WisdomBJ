package com.qun.test.wisdombj.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qun.lib.pollingview.PollingBean;
import com.qun.lib.pollingview.PollingView;
import com.qun.lib.pullrefreshlistview.PullRefreshListView;
import com.qun.test.wisdombj.ConstantValue;
import com.qun.test.wisdombj.R;
import com.qun.test.wisdombj.activity.NewsDetailActivity;
import com.qun.test.wisdombj.bean.NewsDetail;
import com.qun.test.wisdombj.bean.NewsTabData;
import com.qun.test.wisdombj.engine.DataEngine;
import com.qun.test.wisdombj.util.ImageUtil;
import com.qun.test.wisdombj.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/6.
 */

public class TabDetailPager extends BasePager {
    private final static int WHAT_FETCH_DATA_FROM_SERVER = 1;
    private final static int WHAT_FETCH_DATA_FROM_SERVER_ERR = 2;
    private final static int WHAT_LOAD_MORE_DATA_FROM_SERVER = 3;
    private NewsTabData mNewsTabData;
    private NewsDetail mNewsDetail;
    private NewsAdapter mAdapter;
    private List<NewsDetail.News> mNews;
    private PullRefreshListView mPullRefreshListView;
    private PollingView mPollingView;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_FETCH_DATA_FROM_SERVER:
                    mNewsDetail = (NewsDetail) msg.obj;
                    mPollingView.setData(processPollingData(mNewsDetail.data.topnews));
                    mPollingView.startPolling();
                    mNews.clear();
                    mNews.addAll(mNewsDetail.data.news);
                    mAdapter.notifyDataSetChanged();
                    mPullRefreshListView.onRefreshCompleted();
                    break;
                case WHAT_FETCH_DATA_FROM_SERVER_ERR:
                    mPullRefreshListView.onRefreshCompleted();
                    break;
                case WHAT_LOAD_MORE_DATA_FROM_SERVER:
                    mNewsDetail = (NewsDetail) msg.obj;
                    mNews.addAll(mNewsDetail.data.news);
                    mAdapter.notifyDataSetChanged();
                    mPullRefreshListView.onLoadMoreCompleted(false);
                    break;
            }

        }
    };


    private List<PollingBean> processPollingData(final List<NewsDetail.TopNews> topnews) {
        List<PollingBean> data = new ArrayList<>();
        for (int i = 0; i < topnews.size(); i++) {
            PollingBean pollingBean = new PollingBean();
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final String url = topnews.get(i).url;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsDetailActivity.enter(mContext, url);
                }
            });
            ImageUtil.bindImage(imageView, topnews.get(i).topimage);
            pollingBean.imageView = imageView;
            pollingBean.title = topnews.get(i).title;
            data.add(pollingBean);
        }
        return data;
    }


    public TabDetailPager(Context context, NewsTabData newsTabData) {
        super(context);
        mNewsTabData = newsTabData;
    }

    @Override
    public void loadData() {
        String url = ConstantValue.URL_HOST + mNewsTabData.getUrl();
        String cache = (String) SPUtil.getData(url, "");
        if (TextUtils.isEmpty(cache)) {
            fetchDataFromServer(url, WHAT_FETCH_DATA_FROM_SERVER);
        } else {
            fetchDataFromCache(cache);
        }
    }

    public void refreshData() {
        String url = ConstantValue.URL_HOST + mNewsTabData.getUrl();
        fetchDataFromServer(url, WHAT_FETCH_DATA_FROM_SERVER);
    }

    public void loadMoreData() {
        String url = ConstantValue.URL_HOST + mNewsDetail.data.more;
        if (TextUtils.isEmpty(mNewsDetail.data.more)) {
            mPullRefreshListView.onLoadMoreCompleted(true);
            return;
        }
        fetchDataFromServer(url, WHAT_LOAD_MORE_DATA_FROM_SERVER);
    }


    private void fetchDataFromCache(String cache) {
        new Thread(new ProcessDataRunnable(WHAT_FETCH_DATA_FROM_SERVER, null, cache, mHandler)).start();
    }

    private void fetchDataFromServer(final String url, final int what) {
        DataEngine dataEngine = new DataEngine();
        dataEngine.setOnRequestDataListener(new DataEngine.OnRequestDataListener() {
            @Override
            public void onRequestDataError() {
                mHandler.sendEmptyMessage(WHAT_FETCH_DATA_FROM_SERVER_ERR);
            }

            @Override
            public void onProcessData(final String result) {
                new Thread(new ProcessDataRunnable(what, url, result, mHandler)).start();
            }
        });
        dataEngine.requestData(url);
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
            NewsDetail newsDetail = gson.fromJson(result, NewsDetail.class);
            sendMessage(newsDetail, what);
        }

        private void sendMessage(NewsDetail newsDetail, int what) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.what = what;
                message.obj = newsDetail;
                handler.sendMessage(message);
            }
        }
    }


    @Override
    public View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pager_tab_detail, null, false);
        mPullRefreshListView = view.findViewById(R.id.prlv);
        View topNewsView = LayoutInflater.from(mContext).inflate(R.layout.item_top_news, null, false);
        mPollingView = topNewsView.findViewById(R.id.pv);
        mPullRefreshListView.addHeaderView(topNewsView);
        mNews = new ArrayList<>();
        mAdapter = new NewsAdapter();
        mPullRefreshListView.setAdapter(mAdapter);
        mPullRefreshListView.setOnFetchDataListener(new PullRefreshListView.OnFetchDataListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
        mPullRefreshListView.setOnItemClickListener(new PullRefreshListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String readIds = (String) SPUtil.getData(ConstantValue.KEY_READ_IDS, "");
                int newId = mNews.get(position).id;
                if (!readIds.contains(newId + "")) {
                    readIds += newId + ",";
                    SPUtil.putData(ConstantValue.KEY_READ_IDS, readIds);
                }
                ((TextView) view.findViewById(R.id.tv_title)).setTextColor(Color.GRAY);
                ((TextView) view.findViewById(R.id.tv_pubdate)).setTextColor(Color.GRAY);
                NewsDetailActivity.enter(mContext, mNews.get(position).url);

            }
        });
        return view;
    }

    private class NewsAdapter extends BaseAdapter {
        public NewsAdapter() {
        }

        @Override
        public int getCount() {
            return mNews.size();
        }

        @Override
        public Object getItem(int position) {
            return mNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news, null, false);
                viewHolder = new ViewHolder();
                viewHolder.ivNewsPic = convertView.findViewById(R.id.iv_new_pic);
                viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
                viewHolder.tvPubDate = convertView.findViewById(R.id.tv_pubdate);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageUtil.bindImage(viewHolder.ivNewsPic, mNews.get(position).listimage);
            viewHolder.tvTitle.setText(mNews.get(position).title);
            viewHolder.tvPubDate.setText(mNews.get(position).pubdate);
            String ids = (String) SPUtil.getData(ConstantValue.KEY_READ_IDS, "");
            if (ids.contains(mNews.get(position).id + "")) {
                viewHolder.tvTitle.setTextColor(Color.GRAY);
                viewHolder.tvPubDate.setTextColor(Color.GRAY);
            } else {
                viewHolder.tvTitle.setTextColor(Color.BLACK);
                viewHolder.tvPubDate.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView ivNewsPic;
        public TextView tvTitle;
        public TextView tvPubDate;
    }
}
