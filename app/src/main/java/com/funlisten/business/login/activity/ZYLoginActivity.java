package com.funlisten.business.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.event.ZYEventLoginSuc;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.login.presenter.ZYLoginPresenter;
import com.funlisten.business.login.view.ZYLoginFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ZY on 17/6/30.
 */

public class ZYLoginActivity extends ZYBaseFragmentActivity<ZYLoginFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, ZYLoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ZYLoginPresenter(mFragment);
        showTitle("登录");
    }

    @Override
    protected ZYLoginFragment createFragment() {
        return new ZYLoginFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventLoginSuc loginSuc) {
        finish();
    }

}
