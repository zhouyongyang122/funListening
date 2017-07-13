package com.funlisten.business.play.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.play.view.ZYPlayFragment;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayActivity extends ZYBaseFragmentActivity<ZYPlayFragment> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ZYPlayFragment createFragment() {
        return new ZYPlayFragment();
    }
}
