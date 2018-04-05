package com.qun.test.wisdombj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qun.lib.slidermenu.SliderMenu;
import com.qun.test.wisdombj.OnFillMenuDataListener;
import com.qun.test.wisdombj.R;
import com.qun.test.wisdombj.bean.NewsMenu;
import com.qun.test.wisdombj.fragment.BasePageFragment;
import com.qun.test.wisdombj.fragment.GovernmentFragment;
import com.qun.test.wisdombj.fragment.HomeFragment;
import com.qun.test.wisdombj.fragment.NewCenterFragment;
import com.qun.test.wisdombj.fragment.SettingFragment;
import com.qun.test.wisdombj.fragment.WisdomServiceFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnFillMenuDataListener {

    private FrameLayout mFlContentContainer;
    private SliderMenu mSliderMenu;
    private TextView mTvTitle;
    private FragmentManager mFm;
    private List<BasePageFragment> mFragmentList;
    private ImageButton mBtnMenuToggle;
    private ListView mListView;
    private LeftMenuAdapter mAdapter;

    public static void enter(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initFragment();
        initView();
    }

    private void initFragment() {
        mFm = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(NewCenterFragment.newInstance());
        mFragmentList.add(WisdomServiceFragment.newInstance());
        mFragmentList.add(GovernmentFragment.newInstance());
        mFragmentList.add(SettingFragment.newInstance());
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void toggleMenu() {
        if (mSliderMenu.getState() == SliderMenu.SliderMenuState.CLOSE) {
            mSliderMenu.changedState(SliderMenu.SliderMenuState.OPEN);
        } else {
            mSliderMenu.changedState(SliderMenu.SliderMenuState.CLOSE);
        }
    }

    public void showToggleMenu(boolean show) {
        if (show) {
            mBtnMenuToggle.setVisibility(View.VISIBLE);
            mSliderMenu.setCanSlider(true);
        } else {
            mBtnMenuToggle.setVisibility(View.GONE);
            mSliderMenu.setCanSlider(false);
        }
    }

    private List<NewsMenu.NewsMenuData> mNewsMenuData = new ArrayList<>();

    @Override
    public void onFillMenu(List<NewsMenu.NewsMenuData> list) {
        mCurrentMenuPos = 0;
        mNewsMenuData.clear();
        mNewsMenuData.addAll(list);
        mAdapter.notifyDataSetChanged();
        setTitle(mNewsMenuData.get(mCurrentMenuPos).getTitle());
    }

    private void initView() {
        mSliderMenu = findViewById(R.id.slider_menu);
        mTvTitle = findViewById(R.id.tv_title);
        mBtnMenuToggle = findViewById(R.id.iv_menu_toggle);
        mBtnMenuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });
        mFlContentContainer = findViewById(R.id.fl_content_container);
        RadioGroup rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mFm.beginTransaction().replace(R.id.fl_content_container, mFragmentList.get(0), "homeFragment").commit();
                        setTitle(getString(R.string.menu_home));
                        showToggleMenu(false);
                        mFragmentList.get(0).loadData();
                        break;
                    case R.id.rb_new_center:
                        mFm.beginTransaction().replace(R.id.fl_content_container, mFragmentList.get(1), "newCenterFragment").commit();
                        setTitle(getString(R.string.menu_news_center));
                        showToggleMenu(true);
                        mFragmentList.get(1).loadData();
                        break;
                    case R.id.rb_wisdom_service:
                        mFm.beginTransaction().replace(R.id.fl_content_container, mFragmentList.get(2), "wisdomServiceFragment").commit();
                        setTitle(getString(R.string.menu_wisdom_service));
                        showToggleMenu(true);
                        mFragmentList.get(2).loadData();
                        break;
                    case R.id.rb_government:
                        mFm.beginTransaction().replace(R.id.fl_content_container, mFragmentList.get(3), "governmentFragment").commit();
                        setTitle(getString(R.string.menu_government));
                        showToggleMenu(true);
                        mFragmentList.get(3).loadData();
                        break;
                    case R.id.rb_setting:
                        mFm.beginTransaction().replace(R.id.fl_content_container, mFragmentList.get(4), "settingFragment").commit();
                        setTitle(getString(R.string.menu_setting));
                        showToggleMenu(false);
                        mFragmentList.get(4).loadData();
                        break;
                }
            }
        });
        RadioButton menuHome = findViewById(R.id.rb_home);
        menuHome.setChecked(true);
        mListView = findViewById(R.id.lv);
        mAdapter = new LeftMenuAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentMenuPos = position;
                toggleMenu();
                setTitle(mNewsMenuData.get(position).getTitle());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private int mCurrentMenuPos;

    public class LeftMenuAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public LeftMenuAdapter() {
            mInflater = LayoutInflater.from(HomeActivity.this);
        }

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_left_menu, null, false);
                viewHolder = new ViewHolder();
                viewHolder.menuName = convertView.findViewById(R.id.tv_menu_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.menuName.setText(mNewsMenuData.get(position).getTitle());
            if (position == mCurrentMenuPos) {
                // 被选中
                viewHolder.menuName.setEnabled(true);// 文字变为红色
            } else {
                // 未选中
                viewHolder.menuName.setEnabled(false);// 文字变为白色
            }
            return convertView;
        }
    }

    public class ViewHolder {
        TextView menuName;
    }
}
