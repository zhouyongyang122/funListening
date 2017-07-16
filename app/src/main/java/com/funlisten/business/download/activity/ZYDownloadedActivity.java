package com.funlisten.business.download.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.download.presenter.ZYDownloadedPresenter;
import com.funlisten.business.download.view.ZYDownloadedFragment;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadedActivity extends ZYBaseFragmentActivity<ZYDownloadedFragment> {

    static final String ALBUM_ID = "albumId";

    static final String TITLE = "title";

    public static Intent createIntent(Context context, int albumId, String title) {
        Intent intent = new Intent(context, ZYDownloadedActivity.class);
        intent.putExtra(ALBUM_ID, albumId);
        intent.putExtra(TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle(getIntent().getStringExtra(TITLE));
        new ZYDownloadedPresenter(mFragment, getIntent().getIntExtra(ALBUM_ID, 0));
    }

    @Override
    protected ZYDownloadedFragment createFragment() {
        return new ZYDownloadedFragment();
    }
}
