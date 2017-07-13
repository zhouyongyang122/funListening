package com.funlisten.business.download.activity;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.download.view.ZYDownloadingFragment;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadingActivity extends ZYBaseFragmentActivity<ZYDownloadingFragment> {

    @Override
    protected ZYDownloadingFragment createFragment() {
        return new ZYDownloadingFragment();
    }
}
