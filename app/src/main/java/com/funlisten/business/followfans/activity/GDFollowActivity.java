package com.funlisten.business.followfans.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.followfans.model.GDFollowModel;
import com.funlisten.business.followfans.presenter.GDFollowPresenter;
import com.funlisten.business.followfans.view.GDFollowFragment;
import com.funlisten.business.login.model.ZYUserManager;

/**
 * Created by Administrator on 2017/7/12.
 */

public class GDFollowActivity extends ZYBaseFragmentActivity<GDFollowFragment> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("关注");
        new GDFollowPresenter(mFragment,new GDFollowModel(), ZYUserManager.getInstance().getUser().userId);
    }

    @Override
    protected GDFollowFragment createFragment() {
        return new GDFollowFragment();
    }

}
