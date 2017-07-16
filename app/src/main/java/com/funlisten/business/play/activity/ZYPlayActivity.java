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
import com.funlisten.utils.ZYStatusBarUtils;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayActivity extends ZYBaseFragmentActivity<ZYPlayFragment> {

    static final String AUDIO_ID = "audioId";
    static final String ALBUM_ID = "albumID";

    public static Intent createIntent(Context context, int albumId, int audioId) {
        Intent intent = new Intent(context, ZYPlayActivity.class);
        intent.putExtra(AUDIO_ID, audioId);
        intent.putExtra(ALBUM_ID, albumId);
        return intent;
    }

    public static void toPlayActivity(Activity context, int albumId, int audioId) {
        context.startActivity(createIntent(context, albumId, audioId));
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
        new ZYPlayPresenter(mFragment, getIntent().getIntExtra(ALBUM_ID, 0), getIntent().getIntExtra(AUDIO_ID, 0));
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
