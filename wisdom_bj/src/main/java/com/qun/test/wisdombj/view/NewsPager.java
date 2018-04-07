package com.qun.test.wisdombj.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lib.pagetabindicator.TabPageIndicator;
import com.lib.pagetabindicator.TitlePageIndicator;
import com.qun.test.wisdombj.R;
import com.qun.test.wisdombj.bean.NewsTabData;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/6.
 */

public class NewsPager extends BasePager {
    private ArrayList<NewsTabData> datas = new ArrayList<>();
    private ArrayList<TabDetailPager> mDetailPagers = new ArrayList<>();
    private NewsTabAdapter mAdapter;
    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;

    public NewsPager(Context context, ArrayList<NewsTabData> children) {
        super(context);
        datas.clear();
        datas.addAll(children);
        mAdapter = new NewsTabAdapter();
        mViewPager.setAdapter(mAdapter);
        mTabPageIndicator.setViewPager(mViewPager);
    }

    @Override
    public View initView() {
        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pager_news_detail, null, false);
        mTabPageIndicator = view.findViewById(R.id.indicator);
        mViewPager = view.findViewById(R.id.vp);
        ImageButton btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(++pos);
            }
        });
        return view;
    }

    @Override
    public void loadData() {
        mDetailPagers.clear();
        for (int i = 0; i < datas.size(); i++) {
            mDetailPagers.add(new TabDetailPager(mContext, datas.get(i)));
        }
        mAdapter.notifyDataSetChanged();
        mTabPageIndicator.notifyDataSetChanged();
    }

    private class NewsTabAdapter extends PagerAdapter {
        public NewsTabAdapter() {
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return datas.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mDetailPagers.get(position);
            View view = pager.mRootView;
            pager.loadData();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
