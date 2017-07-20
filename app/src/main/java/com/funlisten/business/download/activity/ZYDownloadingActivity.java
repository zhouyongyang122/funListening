package com.funlisten.business.download.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.download.presenter.ZYDownloadingPresenter;
import com.funlisten.business.download.view.ZYDownloadingFragment;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadingActivity extends ZYBaseFragmentActivity<ZYDownloadingFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, ZYDownloadingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("正在下载");
        new ZYDownloadingPresenter(mFragment);
    }

    @Override
    protected ZYDownloadingFragment createFragment() {
        return new ZYDownloadingFragment();
    }
}
