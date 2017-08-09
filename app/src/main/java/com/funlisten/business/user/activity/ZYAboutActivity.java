package com.funlisten.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;

import butterknife.Bind;

/**
 * Created by ZY on 17/8/9.
 */

public class ZYAboutActivity extends ZYBaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ZYAboutActivity.class);
    }


    @Bind(R.id.textAbout)
    TextView textAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zy_activity_about);
        showTitle("关于我们");
        textAbout.setText("产品最后提供文字就好");
    }
}
