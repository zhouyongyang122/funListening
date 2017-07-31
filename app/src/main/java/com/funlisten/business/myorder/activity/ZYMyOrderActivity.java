package com.funlisten.business.myorder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.myorder.model.ZYMyOrderModel;
import com.funlisten.business.myorder.presenter.ZYMyOrderPresenter;
import com.funlisten.business.myorder.view.ZYMyOrderFragment;

/**
 * Created by gd on 2017/7/22.
 * 我的订购 （现改成我的订阅 我的订阅就是我订阅的专辑）
 */

public class ZYMyOrderActivity extends ZYBaseFragmentActivity<ZYMyOrderFragment> {
    int type;

    public static Intent createIntent(Context context,int type){
        Intent intent = new Intent(context,ZYMyOrderActivity.class);
        intent.putExtra("type",type);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("我的订阅");
        type = getIntent().getIntExtra("type",-1);
        if(type == 1) showTitle("我的订阅");
        else showTitle("我的订单");
        new ZYMyOrderPresenter(mFragment,new ZYMyOrderModel(),type);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected ZYMyOrderFragment createFragment() {
        return new ZYMyOrderFragment();
    }
}
