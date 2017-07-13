package com.funlisten.business.download.activity;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.download.view.ZYDownloadedFragment;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadedActivity extends ZYBaseFragmentActivity<ZYDownloadedFragment> {

    @Override
    protected ZYDownloadedFragment createFragment() {
        return new ZYDownloadedFragment();
    }
}
