package com.funlisten.business.album.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.album.presenter.ZYAlbumHomePresenter;
import com.funlisten.business.album.view.ZYAlbumHomeFragment;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.business.play.model.bean.ZYPlayHistory;
import com.funlisten.utils.ZYToast;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYAlbumHomeActivity extends ZYBaseFragmentActivity<ZYAlbumHomeFragment> {

    static final String ALBUM_ID = "album_id";

    ZYAlbumHomePresenter mPresenter;

    public static Intent createIntent(Context context, int albumId) {
        Intent intent = new Intent(context, ZYAlbumHomeActivity.class);
        intent.putExtra(ALBUM_ID, albumId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ZYAlbumHomePresenter(mFragment, getIntent().getIntExtra(ALBUM_ID, 0));

        showActionRightImg2(R.drawable.nav_btn_share_n, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZYToast.show(mActivity, "分享功能还没有实现!");
            }
        });

        showActionRightImg(R.drawable.nav_btn_quick_play_n, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZYPlayHistory history = ZYPlayManager.getInstance().queryLastPlay();
                if (history != null) {
                    ZYPlayActivity.toPlayActivity(mActivity, Integer.parseInt(history.albumId), Integer.parseInt(history.audioId));
                } else {
                    ZYToast.show(mActivity, "您还没有播放过任何音频哦,请先去选择要播放的音频!");
                }
            }
        });
    }

    @Override
    protected ZYAlbumHomeFragment createFragment() {
        return new ZYAlbumHomeFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}
