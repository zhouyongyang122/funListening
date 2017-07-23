package com.funlisten.business.dailylisten.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.dailylisten.model.ZYDailyListenModel;
import com.funlisten.business.dailylisten.presenter.ZYDailyListenPresenter;
import com.funlisten.business.dailylisten.view.ZYDailyFragment;

/**
 * Created by gd on 2017/7/20.
 */

public class ZYDailyListenActivity extends ZYBaseFragmentActivity<ZYDailyFragment> {
    int albumId;
    ZYDailyListenPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("   ");
        showRightImg2(true);
        showRightImg(true);
        albumId = getIntent().getIntExtra("albumId",-1);
        presenter =  new ZYDailyListenPresenter(mFragment,new ZYDailyListenModel(),albumId);
        presenter.loadData();
        mFragment.setPresenter(presenter);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    public void showChangeBar(boolean isShow){
        if(isShow){
            showTitle("   ");
            showRightImg2(true);
        }else {
            showTitle("每日精听|免费 ");
            showRightImg2(false);
        }
    }


    @Override
    protected ZYDailyFragment createFragment() {
        return new ZYDailyFragment();
    }
}
