package com.funlisten.business.dailylisten.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.business.dailylisten.model.ZYDailyListenModel;
import com.funlisten.business.dailylisten.presenter.ZYDailyListenPresenter;
import com.funlisten.business.dailylisten.view.ZYDailyFragment;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.thirdParty.image.ZYIImageLoader;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.SRShareUtils;
import com.third.loginshare.entity.ShareEntity;

/**
 * Created by gd on 2017/7/20.
 */

public class ZYDailyListenActivity extends ZYBaseFragmentActivity<ZYDailyFragment> {
    int albumId;
    ZYDailyListenPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("   ");
        albumId = getIntent().getIntExtra("albumId",-1);
        presenter =  new ZYDailyListenPresenter(mFragment,new ZYDailyListenModel(),albumId);
        presenter.loadData();
        mFragment.setPresenter(presenter);
        init();
    }

    void init(){
        showActionRightImg2(R.drawable.share_slelector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        showActionRightImg(R.drawable.quick_play_slelector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

//    private void share(final SRMarkBean markBean) {
//        ShareEntity shareEntity = new ShareEntity();
//        shareEntity.avatarUrl = ZYUserManager.getInstance().getUser().avatar;
//        if (bitmap != null) {
//            shareEntity.avatarBitmap = bitmap;
//        } else {
//            shareEntity.avatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        }
//        shareEntity.webUrl = markBean.share_url;
//        shareEntity.title = ZYUserManager.getInstance().getUser().nickname + "的录音作品快来听一下吧!";
//        shareEntity.text = "专为小学生设计的智能学习机";
//        new SRShareUtils(mActivity, shareEntity).share();
//    }

    @Override
    public void setPresenter(Object presenter) {

    }

    public void showChangeBar(boolean isShow){
        if(isShow){
            showTitle("   ");
            showRightImg2(true);
        }else {
            showTitle("每日精听|免费 ");
            showRightImg2(false);
        }
    }


    @Override
    protected ZYDailyFragment createFragment() {
        return new ZYDailyFragment();
    }
}
