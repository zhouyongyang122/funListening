package com.funlisten.business.myorder.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.myorder.model.ZYMyOrderModel;
import com.funlisten.business.myorder.presenter.ZYMyOrderPresenter;
import com.funlisten.business.myorder.view.ZYMyOrderFragment;

/**
 * Created by gd on 2017/7/22.
 * 我的订购
 */

public class ZYMyOrderActivity extends ZYBaseFragmentActivity<ZYMyOrderFragment> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("我的订购");
        new ZYMyOrderPresenter(mFragment,new ZYMyOrderModel(),"album");
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected ZYMyOrderFragment createFragment() {
        return new ZYMyOrderFragment();
    }
}
