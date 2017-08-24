package com.funlisten.business.mylike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.mylike.model.ZYMyLikeModel;
import com.funlisten.business.mylike.presenter.ZYMylikePresenter;
import com.funlisten.business.mylike.view.ZYMyLikeFragment;

/**
 * Created by gd on 2017/7/25.
 * 我喜欢的(就是我的收藏 只有音频列表)
 */

public class ZYMyLikeActivity extends ZYBaseFragmentActivity<ZYMyLikeFragment> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("我的收藏");
        new ZYMylikePresenter(mFragment,new ZYMyLikeModel());
    }

    @Override
    protected ZYMyLikeFragment createFragment() {
        return  new ZYMyLikeFragment();
    }
}
