package com.funlisten.business.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.utils.ZYUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/8/27.
 */

public class ZYAgreementActivity extends ZYBaseActivity {

    @Bind(R.id.textValue)
    TextView textValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zy_activity_agreement);

        showTitle("使用协议与版权申明");

        textValue.setText(ZYUtils.getRawContent(mActivity, R.raw.disclaimer));
    }
}
