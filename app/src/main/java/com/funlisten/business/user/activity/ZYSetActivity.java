package com.funlisten.business.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.user.view.ZYSetFragment;

/**
 * Created by ZY on 17/7/21.
 */

public class ZYSetActivity extends ZYBaseFragmentActivity<ZYSetFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, ZYSetActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ZYSetFragment createFragment() {
        return new ZYSetFragment();
    }
}
