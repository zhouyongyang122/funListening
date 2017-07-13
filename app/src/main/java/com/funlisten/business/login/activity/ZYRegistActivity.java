package com.funlisten.business.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.login.presenter.ZYRegistPresenter;
import com.funlisten.business.login.view.ZYRegistFragment;

/**
 * Created by ZY on 17/7/1.
 */

public class ZYRegistActivity extends ZYBaseFragmentActivity<ZYRegistFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, ZYRegistActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ZYRegistPresenter(mFragment);
        showTitle("注册");
    }

    @Override
    protected ZYRegistFragment createFragment() {
        return new ZYRegistFragment();
    }

    @Override
    public void onBackPressed() {
        if (mFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
