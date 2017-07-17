package com.funlisten.business.play.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.play.model.ZYPLayManager;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.business.play.presenter.ZYPlayPresenter;
import com.funlisten.business.play.view.ZYPlayFragment;
import com.funlisten.utils.ZYStatusBarUtils;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayActivity extends ZYBaseFragmentActivity<ZYPlayFragment> {

    static final String AUDIO_ID = "audioId";
    static final String ALBUM_ID = "albumID";
    static final String IS_NEW_PLAY = "isNewPlay";

//    public static Intent createIntent(Context context, int albumId, int audioId) {
//        return createIntent(context, albumId, audioId, true);
//    }

    public static Intent createIntent(Context context, int albumId, int audioId, boolean isNewPlay) {
        Intent intent = new Intent(context, ZYPlayActivity.class);
        intent.putExtra(AUDIO_ID, audioId);
        intent.putExtra(ALBUM_ID, albumId);
        intent.putExtra(IS_NEW_PLAY, isNewPlay);
        return intent;
    }

    public static void toPlayActivity(Activity context, int albumId, int audioId) {
        toPlayActivity(context, albumId, audioId, true);
    }

    public static void toPlayActivity(Activity context, int albumId, int audioId, boolean isNewPlay) {
        context.startActivity(createIntent(context, albumId, audioId, isNewPlay));
        context.overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        ZYStatusBarUtils.immersiveStatusBar(this, 1);
        if (ZYStatusBarUtils.isCanLightStatusBar()) {
            ZYStatusBarUtils.tintStatusBar(this, Color.TRANSPARENT, 0);
        }
        boolean isNewPlay = getIntent().getBooleanExtra(IS_NEW_PLAY, true);
        int albumId = getIntent().getIntExtra(ALBUM_ID, 0);
        int audioId = getIntent().getIntExtra(AUDIO_ID, 0);
        ZYPlay play = ZYPLayManager.getInstance().getPlay();
        if (isNewPlay) {
            new ZYPlayPresenter(mFragment, getIntent().getIntExtra(ALBUM_ID, 0), getIntent().getIntExtra(AUDIO_ID, 0), true);
        } else {
            if (play != null && play.albumDetail.id == albumId && play.audio.id == audioId) {
                new ZYPlayPresenter(mFragment, getIntent().getIntExtra(ALBUM_ID, 0), getIntent().getIntExtra(AUDIO_ID, 0), false);
            } else {
                new ZYPlayPresenter(mFragment, getIntent().getIntExtra(ALBUM_ID, 0), getIntent().getIntExtra(AUDIO_ID, 0), true);
            }
        }
    }

    @Override
    protected ZYPlayFragment createFragment() {
        return new ZYPlayFragment();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.slide_down);
    }
}
