package com.funlisten.business.play.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.play.presenter.ZYPlayPresenter;
import com.funlisten.business.play.view.ZYPlayFragment;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYStatusBarUtils;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayActivity extends ZYBaseFragmentActivity<ZYPlayFragment> {

    static final String AUDIO_ID = "audioId";
    static final String ALBUM_ID = "albumID";
    static final String SORT_TYPE = "sortType";

    ZYPlayPresenter mPresenter;

    public static Intent createIntent(Context context, int albumId, int audioId, String sortType) {
        Intent intent = new Intent(context, ZYPlayActivity.class);
        intent.putExtra(AUDIO_ID, audioId);
        intent.putExtra(ALBUM_ID, albumId);
        intent.putExtra(SORT_TYPE, sortType);
        return intent;
    }

    public static void toPlayActivity(Activity context, int albumId, int audioId, String sortType) {
        context.startActivity(createIntent(context, albumId, audioId, sortType));
        context.overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

    public static void toPlayActivity(Activity context, int albumId, int audioId) {
        context.startActivity(createIntent(context, albumId, audioId, null));
        context.overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

    public static void toPlayActivity(Activity context) {
        context.startActivity(createIntent(context, 0, 0, null));
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ZYPlayActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        ZYStatusBarUtils.immersiveStatusBar(this, 1);
        if (ZYStatusBarUtils.isCanLightStatusBar()) {
            ZYStatusBarUtils.tintStatusBar(this, Color.TRANSPARENT, 0);
        }
        int mAlbumId = getIntent().getIntExtra(ALBUM_ID, 0);
        int mAudioId = getIntent().getIntExtra(AUDIO_ID, 0);
        String sortType = getIntent().getStringExtra(SORT_TYPE);
        mPresenter = new ZYPlayPresenter(mFragment, mAlbumId, mAudioId, sortType);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int mAlbumId = intent.getIntExtra(ALBUM_ID, 0);
        int mAudioId = intent.getIntExtra(AUDIO_ID, 0);
        ZYLog.e(getClass().getSimpleName(), "onNewIntent: " + mAlbumId + ":" + mAudioId);
        if (mAlbumId > 0) {
            mPresenter.refreshPlay(mAlbumId, mAudioId);
        }
    }

    @Override
    protected ZYPlayFragment createFragment() {
        return new ZYPlayFragment();
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, R.anim.slide_down);
        moveTaskToBack(true);
    }
}
