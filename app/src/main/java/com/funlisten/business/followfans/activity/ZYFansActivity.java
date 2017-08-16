package com.funlisten.business.followfans.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.followfans.model.ZYFollowModel;
import com.funlisten.business.followfans.presenter.ZYFollowPresenter;
import com.funlisten.business.followfans.view.ZYFollowFragment;
import com.funlisten.business.login.model.ZYUserManager;

/**
 * Created by gd on 2017/7/12.
 */

public class ZYFansActivity extends ZYBaseFragmentActivity<ZYFollowFragment> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("粉丝");
        new ZYFollowPresenter(mFragment, ZYUserManager.getInstance().getUser().userId, ZYFollowModel.FANS_TYPE);
    }

    @Override
    protected ZYFollowFragment createFragment() {
        return new ZYFollowFragment();
    }

}
