package com.qun.test.wisdombj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qun.test.wisdombj.ConstantValue;
import com.qun.test.wisdombj.R;
import com.qun.test.wisdombj.util.SPUtil;

public class GuideActivity extends AppCompatActivity {
    private final int[] imgIds = new int[]{
            R.drawable.guide_1,
            R.drawable.guide_2,
            R.drawable.guide_3
    };
    private ImageView[] mGuideViews;
    private ViewPager mViewPager;
    private View mFocusDot;
    private int mDotMoveDistance;
    private Button mBtnStart;

    public static void enter(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        setContentView(R.layout.activity_guide);
        initViews();
        initData();
        fillData();
    }

    private void fillData() {
        GuideAdapter adapter = new GuideAdapter();
        mViewPager.setAdapter(adapter);
    }

    private void initData() {
        mGuideViews = new ImageView[3];
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imgIds[i]);
            mGuideViews[i] = imageView;
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFocusDot.getLayoutParams();
            lp.leftMargin = (int) (position * mDotMoveDistance + mDotMoveDistance * positionOffset);
            mFocusDot.setLayoutParams(lp);
        }

        @Override
        public void onPageSelected(int position) {
            if (position == imgIds.length - 1) {
                mBtnStart.setVisibility(View.VISIBLE);
            } else {
                mBtnStart.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initViews() {
        mViewPager = findViewById(R.id.vp);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        initDots();
        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.putData(ConstantValue.KEY_GUIDE_FINISH, true);
                HomeActivity.enter(GuideActivity.this);
                finish();
            }
        });
    }

    private void initDots() {
        final LinearLayout linearLayout = findViewById(R.id.ll_normal_dots_container);
        for (int i = 0; i < imgIds.length; i++) {
            View normalDot = new View(this);
            normalDot.setBackgroundResource(R.drawable.gudie_dot_normal);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    (int) getResources().getDimension(R.dimen.dot_size),
                    (int) getResources().getDimension(R.dimen.dot_size));
            if (i > 0) {
                lp.leftMargin = (int) getResources().getDimension(R.dimen.dot_left_margin);
            }
            normalDot.setLayoutParams(lp);
            linearLayout.addView(normalDot);
        }
        mFocusDot = new View(this);
        mFocusDot.setBackgroundResource(R.drawable.gudie_dot_focus);
        RelativeLayout relativeLayout = findViewById(R.id.rl_focus_dot_container);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.dot_size),
                (int) getResources().getDimension(R.dimen.dot_size));

        mFocusDot.setLayoutParams(layoutParams);
        relativeLayout.addView(mFocusDot);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mDotMoveDistance = linearLayout.getChildAt(1).getLeft() - linearLayout.getChildAt(0).getLeft();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
    }

    private class GuideAdapter extends PagerAdapter {
        public GuideAdapter() {
        }

        @Override
        public int getCount() {
            return mGuideViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView img = mGuideViews[position];
            container.addView(img);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }
}
