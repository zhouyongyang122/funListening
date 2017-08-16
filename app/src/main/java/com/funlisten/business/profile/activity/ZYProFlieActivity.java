package com.funlisten.business.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.profile.model.ZYProfileModel;
import com.funlisten.business.profile.presenter.ZYProfilePresenter;
import com.funlisten.business.profile.view.ZYProfileFragment;

/**
 * Created by gd on 2017/7/16.
 * 我的专辑
 */

public class ZYProFlieActivity extends ZYBaseFragmentActivity<ZYProfileFragment> {

    public static Intent createIntent(Context context, String uid) {
        Intent intent = new Intent(context, ZYProFlieActivity.class);
        intent.putExtra("uid", uid);
        return intent;
    }

    ZYProfilePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ZYProfilePresenter(mFragment, new ZYProfileModel(), 0, getIntent().getStringExtra("uid"));
        showTitle(" ");
//        showActionRightImg(R.drawable.nav_btn_share_n, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }


    @Override
    protected ZYProfileFragment createFragment() {
        return new ZYProfileFragment();
    }
}
