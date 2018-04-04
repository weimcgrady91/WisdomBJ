package com.qun.test.wisdombj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.qun.lib.slidermenu.SliderMenu;
import com.qun.test.wisdombj.R;
import com.qun.test.wisdombj.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    private FrameLayout mFlContentContainer;
    private SliderMenu mSliderMenu;

    public static void enter(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        mFlContentContainer = findViewById(R.id.fl_content_container);
        HomeFragment homeFragment = HomeFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_content_container, homeFragment, "homeFragment").commit();
        mSliderMenu = findViewById(R.id.slider_menu);
        findViewById(R.id.iv_menu_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSliderMenu.getState() == SliderMenu.SliderMenuState.CLOSE) {
                    mSliderMenu.changedState(SliderMenu.SliderMenuState.OPEN);
                } else {
                    mSliderMenu.changedState(SliderMenu.SliderMenuState.CLOSE);
                }
            }
        });
    }
}
