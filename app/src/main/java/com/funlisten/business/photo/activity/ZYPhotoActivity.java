package com.funlisten.business.photo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.base.mvp.ZYBaseFragmentActivity;
import com.funlisten.base.view.ZYPicSelect;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.photo.model.ZYPhotoModel;
import com.funlisten.business.photo.presenter.ZYPhotoPresenter;
import com.funlisten.business.photo.view.ZYPhotoFragment;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYToast;
import com.funlisten.utils.ZYUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by gd on 2017/7/14.
 */

public class ZYPhotoActivity extends ZYBaseFragmentActivity<ZYPhotoFragment> implements  ZYPicSelect.PicSelectListener{

    ZYPicSelect picSelect;
    Button button;
    ZYPhotoPresenter zyPhotoPresenter;
    private boolean isEdit = false;
    private String userId;
    public static Intent createIntent(Context context ,String userId){
        Intent intent = new Intent(context, ZYPhotoActivity.class);
        intent.putExtra("userId", userId);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("相册");
        userId = getIntent().getStringExtra("userId");
        zyPhotoPresenter = new ZYPhotoPresenter(mFragment,new ZYPhotoModel(),userId);
        button = (Button) LayoutInflater.from(this).inflate(R.layout.photo_delete,null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE );
        button.setLayoutParams(layoutParams);
        mRootView.addView(button);
        button.setVisibility(View.GONE);
        String localId = ZYUserManager.getInstance().getUser().id;
        if(userId.equals(localId)){
            initView();
        }

    }
    private void initView(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragment.getPhoteList().size() <=0 ){
                    ZYToast.show(ZYPhotoActivity.this,"没有选择任何照片");
                    return;
                }
                button.setVisibility(View.GONE);
                showRightImg2(true);
                setRighImgPhoto(R.drawable.icon_select_n);
                isEdit = !isEdit;
                mFragment.refreshPhoto(isEdit);
                zyPhotoPresenter.deletePhoto(mFragment.getPhoteList());
            }
        });
        showActionRightImg(R.drawable.icon_select_n, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEdit){
                    mFragment.clearPhoto();
                    button.setVisibility(View.VISIBLE);
                    showRightImg2(false);
                    setRighImgPhoto(R.drawable.icon_cancel_n);
                    isEdit = !isEdit;
                    mFragment.refreshPhoto(isEdit);
                }else {
                    mFragment.clearPhoto();
                    button.setVisibility(View.GONE);
                    showRightImg2(true);
                    setRighImgPhoto(R.drawable.icon_select_n);
                    isEdit = !isEdit;
                    mFragment.refreshPhoto(isEdit);
                }

            }
        });
        showActionRightImg2(R.drawable.icon_add_n, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picSelect == null) {
                    picSelect = new ZYPicSelect(mActivity, ZYPhotoActivity.this);
                }
                picSelect.showSelectDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picSelect.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected ZYPhotoFragment createFragment() {
        return new ZYPhotoFragment();
    }

    @Override
    public void onPicSelected(Uri uri) {
        File file = new File(ZYApplication.IMG_CACHE_DIR+"temp.png");
        ZYUtils.compressToSize(uri.getPath(),file,100*1024);
        zyPhotoPresenter.upLoadPhoto(file);
    }
}
