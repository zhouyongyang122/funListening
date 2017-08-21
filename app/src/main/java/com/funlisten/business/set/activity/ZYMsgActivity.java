package com.funlisten.business.set.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.set.model.ZYSetModel;
import com.funlisten.business.set.presenter.SRSysMsgPresenter;
import com.funlisten.business.set.view.ZYMsgFragment;

/**
 * Created by ZY on 17/4/9.
 */

public class ZYMsgActivity extends ZYBaseFragmentActivity<ZYMsgFragment> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SRSysMsgPresenter(mFragment, new ZYSetModel());
        mActionBar.showTitle("消息");
    }

    @Override
    protected ZYMsgFragment createFragment() {
        return new ZYMsgFragment();
    }
}
