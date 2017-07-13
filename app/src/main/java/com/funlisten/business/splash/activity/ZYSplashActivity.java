package com.funlisten.business.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.business.main.activity.ZYMainActivity;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/27.
 */

public class ZYSplashActivity extends ZYBaseActivity {

    @Bind(R.id.imgSplash)
    ImageView imgSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zy_activity_splash);
        hideActionBar();
        imgSplash.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mActivity, ZYMainActivity.class));
                finish();
            }
        }, 1000);
    }

    @Override
    protected boolean tintStatusBar() {
        return false;
    }
}
