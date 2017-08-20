package com.funlisten.business.album.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.album.presenter.ZYAlbumListPresenter;
import com.funlisten.business.album.view.ZYAlbumListFragment;

/**
 * Created by ZY on 17/8/18.
 */

public class ZYMyAlbumListActivity extends ZYBaseFragmentActivity<ZYAlbumListFragment> {

    public static Intent createIntent(Context context){
        return new Intent(context,ZYMyAlbumListActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("我的专辑列表");
        new ZYAlbumListPresenter(mFragment);
    }

    @Override
    protected ZYAlbumListFragment createFragment() {
        return new ZYAlbumListFragment();
    }
}
