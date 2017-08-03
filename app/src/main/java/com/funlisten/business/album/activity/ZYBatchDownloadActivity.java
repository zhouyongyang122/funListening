package com.funlisten.business.album.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.view.ZYBatchDownloadFragment;
import com.funlisten.business.play.model.bean.ZYAudio;

import java.util.ArrayList;

/**
 * Created by gd on 2017/7/26.
 */

public class ZYBatchDownloadActivity extends ZYBaseFragmentActivity<ZYBatchDownloadFragment> {
    public static Intent createIntent(Context context, ArrayList<ZYAudio> list, ZYAlbumDetail albumDetail){
        Intent intent = new Intent(context,ZYBatchDownloadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("audiolist",list);
        bundle.putSerializable("album",albumDetail);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putSerializable("audiolist",(ArrayList<ZYAudio>)getIntent().getSerializableExtra("audiolist"));
        bundle.putSerializable("album",getIntent().getSerializableExtra("album"));
        mFragment.setArguments(bundle);
        showTitle("批量下载");
    }

    @Override
    protected ZYBatchDownloadFragment createFragment() {
        return new ZYBatchDownloadFragment();
    }
}
