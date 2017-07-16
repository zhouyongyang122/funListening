package com.funlisten.business.download.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.download.presenter.ZYDownloadHomePresenter;
import com.funlisten.business.download.view.ZYDownloadHomeFragment;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYDownloadHomeActivity extends ZYBaseFragmentActivity<ZYDownloadHomeFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, ZYDownloadHomeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("我的下载");
        new ZYDownloadHomePresenter(mFragment);
    }

    @Override
    protected ZYDownloadHomeFragment createFragment() {
        return new ZYDownloadHomeFragment();
    }
}
