package com.funlisten.business.album.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.funlisten.R;
import com.funlisten.ZYAppConstants;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.album.presenter.ZYAlbumHomePresenter;
import com.funlisten.business.album.view.ZYAlbumHomeFragment;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.business.play.model.bean.ZYPlayHistory;
import com.funlisten.thirdParty.image.ZYIImageLoader;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.SRShareUtils;
import com.funlisten.utils.ZYToast;
import com.third.loginshare.entity.ShareEntity;

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
                ZYImageLoadHelper.getImageLoader().loadFromMediaStore(this, mPresenter.getAlbumDetail().coverUrl, new ZYIImageLoader.OnLoadLocalImageFinishListener() {
                    @Override
                    public void onLoadFinish(@Nullable final Bitmap bitmap) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShareEntity shareEntity = new ShareEntity();
                                shareEntity.avatarUrl = mPresenter.getAlbumDetail().coverUrl;
                                if (bitmap != null) {
                                    shareEntity.avatarBitmap = bitmap;
                                } else {
                                    shareEntity.avatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                }
                                shareEntity.webUrl = ZYAppConstants.getShareUrl(mPresenter.getAlbumDetail().id);
                                shareEntity.title = mPresenter.getAlbumDetail().name;
                                shareEntity.text = mPresenter.getAlbumDetail().title;
                                new SRShareUtils(mActivity, shareEntity).share();
                            }
                        });
                    }
                });
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
