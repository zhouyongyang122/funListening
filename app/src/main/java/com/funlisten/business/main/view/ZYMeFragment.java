package com.funlisten.business.main.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventLoginOutSuc;
import com.funlisten.base.event.ZYEventLoginSuc;
import com.funlisten.base.event.ZYEventUpdateUserInfo;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.view.ZYPicSelect;
import com.funlisten.business.accountmanage.activity.ZYAccountManageActivity;
import com.funlisten.business.album.activity.ZYMyAlbumListActivity;
import com.funlisten.business.download.activity.ZYDownloadHomeActivity;
import com.funlisten.business.followfans.activity.ZYFansActivity;
import com.funlisten.business.followfans.activity.ZYFollowActivity;
import com.funlisten.business.main.activity.ZYFeedBackActivity;
import com.funlisten.business.main.contract.ZYMeContract;
import com.funlisten.business.mylike.activity.ZYMyLikeActivity;
import com.funlisten.business.myorder.activity.ZYMyOrderActivity;
import com.funlisten.business.photo.activity.ZYPhotoActivity;
import com.funlisten.business.profile.activity.ZYProFlieActivity;
import com.funlisten.business.user.activity.ZYSetActivity;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.login.model.bean.ZYUser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/5/11.
 */

public class ZYMeFragment extends ZYBaseFragment<ZYMeContract.IPresenter> implements ZYMeContract.IView, ZYPicSelect.PicSelectListener {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textLoginTip)
    TextView textLoginTip;

    @Bind(R.id.layoutFollowAndFans)
    LinearLayout layoutFollowAndFans;

    @Bind(R.id.textFollows)
    TextView textFollows;

    @Bind(R.id.textFans)
    TextView textFans;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zy_fragment_me, container, false);
        ButterKnife.bind(this, view);
        initUserInfo();
        return view;
    }

    private void initUserInfo() {
        if (ZYUserManager.getInstance().isGuesterUser(false)) {
            imgAvatar.setImageResource(R.drawable.def_avatar);
            textName.setText("点击登录");
            textLoginTip.setVisibility(View.VISIBLE);
            layoutFollowAndFans.setVisibility(View.GONE);
        } else {
            ZYUser user = ZYUserManager.getInstance().getUser();
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, user.avatarUrl, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(user.nickname);
            textLoginTip.setVisibility(View.GONE);
            layoutFollowAndFans.setVisibility(View.VISIBLE);
            textFollows.setText(user.follow + " 关注");
            textFans.setText(user.fans + "粉丝");
        }
    }

    @OnClick({R.id.imgAvatar, R.id.textName, R.id.textFollows, R.id.textFans
            , R.id.layoutPic, R.id.layoutDowload, R.id.layoutAlbum, R.id.layoutLove, R.id.layoutSub
            , R.id.layoutStu, R.id.layoutBuy, R.id.layoutAccountManager, R.id.layoutHelper, R.id.layoutSet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAvatar:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                break;
            case R.id.textName:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                break;
            case R.id.textFollows:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(new Intent(mActivity, ZYFollowActivity.class));
                break;
            case R.id.textFans:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(new Intent(mActivity, ZYFansActivity.class));
                break;
            case R.id.layoutPic:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(ZYPhotoActivity.createIntent(mActivity, ZYUserManager.getInstance().getUser().userId));
                break;
            case R.id.layoutDowload:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(ZYDownloadHomeActivity.createIntent(mActivity));
                break;
            case R.id.layoutAlbum:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(ZYMyAlbumListActivity.createIntent(mActivity));
                break;
            case R.id.layoutLove:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(new Intent(mActivity, ZYMyLikeActivity.class));
                break;
            case R.id.layoutSub:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(ZYMyOrderActivity.createIntent(mActivity, 1));
                break;
            case R.id.layoutStu:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                break;
            case R.id.layoutBuy:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(ZYMyOrderActivity.createIntent(mActivity, 2));
                break;
            case R.id.layoutAccountManager:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(new Intent(mActivity, ZYAccountManageActivity.class));
                break;
            case R.id.layoutHelper:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                startActivity(new Intent(mActivity,ZYFeedBackActivity.class));
                break;
            case R.id.layoutSet:
                mActivity.startActivity(new Intent(mActivity, ZYSetActivity.class));
                break;
        }
    }

    @Override
    public void refreshUserInfo() {
        initUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventLoginSuc loginSuc) {
//        mPresenter.getUserInfo();
        initUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventLoginOutSuc loginOutSuc) {
        initUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventUpdateUserInfo updateUserInfo) {
        mPresenter.getUserInfo();
    }

    @Override
    public void onPicSelected(Uri uri) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getUserInfo();
    }
}
