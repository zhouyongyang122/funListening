package com.funlisten.business.play.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.play.view.ZYPlayFragment;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayActivity extends ZYBaseFragmentActivity<ZYPlayFragment> {

    static final String AUDIO_ID = "audioId";

    public static Intent createIntent(Context context, String audioId) {
        Intent intent = new Intent(context, ZYPlayActivity.class);
        intent.putExtra(AUDIO_ID, audioId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ZYPlayFragment createFragment() {
        return new ZYPlayFragment();
    }
}
