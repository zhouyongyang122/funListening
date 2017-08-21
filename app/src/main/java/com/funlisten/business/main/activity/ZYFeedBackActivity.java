package com.funlisten.business.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.funlisten.R;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.service.net.ZYNetManager;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.ZYToast;

import java.util.HashMap;

import butterknife.Bind;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/4/9.
 */

public class ZYFeedBackActivity extends ZYBaseActivity {

    @Bind(R.id.textMsg)
    TextView textMsg;

    @Bind(R.id.textOk)
    TextView textOk;

    CompositeSubscription subscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_feedback);

        mActionBar.showTitle("反馈");
        textMsg.setFilters(new InputFilter[]{new InputFilter.LengthFilter(240)});

        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    ZYToast.show(ZYFeedBackActivity.this, "反馈内容不能为空!");
                    return;
                }
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("content", msg);
                showProgress();
//                subscription.add(ZYNetSubscription.subscription(ZYNetManager.shareInstance().getApi().feedBack(params), new ZYNetSubscriber<ZYResponse>() {
//                    @Override
//                    public void onSuccess(ZYResponse response) {
//                        super.onSuccess(response);
//                        hideProgress();
//                        ZYToast.show(ZYFeedBackActivity.this, "反馈成功!");
//                        finish();
//                    }
//
//                    @Override
//                    public void onFail(String message) {
//                        super.onFail(message);
//                        hideProgress();
//                    }
//                }));
            }
        });
    }
}
