package com.funlisten.business.album.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.album.presenter.ZYAlbumListHomePresenter;
import com.funlisten.business.album.view.ZYAlbumListHomeFragment;

/**
 * Created by ZY on 17/6/13.
 */

public class ZYAlbumListHomeActivity extends ZYBaseFragmentActivity<ZYAlbumListHomeFragment> {

    public static Intent createIntent(Context context) {
        return new Intent(context, ZYAlbumListHomeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ZYAlbumListHomePresenter(mFragment);
    }

    @Override
    protected ZYAlbumListHomeFragment createFragment() {
        return new ZYAlbumListHomeFragment();
    }
}
