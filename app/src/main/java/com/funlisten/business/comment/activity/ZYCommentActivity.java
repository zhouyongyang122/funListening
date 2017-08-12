package com.funlisten.business.comment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.comment.model.ZYCommentModel;
import com.funlisten.business.comment.presenter.ZYCommentPresenter;
import com.funlisten.business.comment.view.ZYCommentFragment;
import com.funlisten.business.play.activity.ZYPlayActivity;

/**
 * Created by gd on 2017/7/25.
 * 评论列表
 */

public class ZYCommentActivity extends ZYBaseFragmentActivity<ZYCommentFragment> {

    public static Intent createIntent(Context context, String type, String objectId) {
        Intent intent = new Intent(context, ZYCommentActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("objectId", objectId);
        return intent;
    }

    public static Intent createIntent(Context context, String type, String objectId, boolean isBackToPlay) {
        Intent intent = new Intent(context, ZYCommentActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("objectId", objectId);
        intent.putExtra("isBackToPlay", isBackToPlay);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ZYCommentPresenter(mFragment, new ZYCommentModel(), getIntent().getStringExtra("type"), getIntent().getStringExtra("objectId"));
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected ZYCommentFragment createFragment() {
        return new ZYCommentFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getBooleanExtra("isBackToPlay", false)) {
            ZYPlayActivity.toPlayActivity(mActivity,true);
        }
    }
}
