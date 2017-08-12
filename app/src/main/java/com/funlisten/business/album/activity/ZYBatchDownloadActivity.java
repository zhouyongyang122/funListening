package com.funlisten.business.album.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.presenter.ZYBatchDownPresenter;
import com.funlisten.business.album.view.ZYBatchDownloadFragment;
import com.funlisten.business.play.model.bean.ZYAudio;

import java.util.ArrayList;

/**
 * Created by gd on 2017/7/26.
 */

public class ZYBatchDownloadActivity extends ZYBaseFragmentActivity<ZYBatchDownloadFragment> {
    ZYAlbumDetail albumDetail;
    int totalCount;
    public static Intent createIntent(Context context,ZYAlbumDetail albumDetail,int totalCount){
        Intent intent = new Intent(context,ZYBatchDownloadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("album",albumDetail);
        bundle.putInt("totalCount",totalCount);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumDetail = (ZYAlbumDetail) getIntent().getSerializableExtra("album");
        totalCount = getIntent().getIntExtra("totalCount",-1);
        new ZYBatchDownPresenter(mFragment,albumDetail,totalCount);
        showTitle("批量下载");
    }

    @Override
    protected ZYBatchDownloadFragment createFragment() {
        return new ZYBatchDownloadFragment();
    }
}
