package com.funlisten.business.profile.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.business.album.activity.ZYAlbumHomeActivity;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.photo.activity.ZYPhotoActivity;
import com.funlisten.business.profile.contract.ZYProfileContract;
import com.funlisten.business.profile.contract.ZYProfileFollowPhoto;
import com.funlisten.business.profile.model.bean.ZYProfileHeaderInfo;
import com.funlisten.business.profile.view.viewholder.ZYProfileHeader;
import com.funlisten.business.profile.view.viewholder.ZYProfileVH;
import com.funlisten.utils.ZYResourceUtils;

/**
 * Created by gd on 2017/7/16.
 */

public class ZYProfileFragment extends ZYListDateFragment<ZYProfileContract.IPresenter,ZYAlbumDetail>implements ZYProfileContract.IView {

    ZYProfileHeader profileHeader;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c8));
        ZYProfileHeader.followPhoto = followPhoto;
        profileHeader = new ZYProfileHeader();
        mAdapter.addHeader(profileHeader);
        return view;
    }

    private ZYProfileFollowPhoto followPhoto = new ZYProfileFollowPhoto() {
        @Override
        public void onFollow(String userId) {
            mPresenter.follow(userId);
        }

        @Override
        public void intoPhoto(String userId) {
            Intent intent = new Intent(mActivity, ZYPhotoActivity.class);
            intent.putExtra("userId",userId);
            mActivity.startActivity(intent);
        }
    };


    @Override
    protected void onItemClick(View view, int position) {
        mActivity.startActivity(ZYAlbumHomeActivity.createIntent(mActivity,mPresenter.getDataList().get(position).id));
    }


    @Override
    protected ZYProfileVH createViewHolder() {
        return new ZYProfileVH();
    }

    @Override
    public void refreshView() {
        mAdapter.notifyDataSetChanged();
        profileHeader.updateView(new ZYProfileHeaderInfo(mPresenter.getUser(),mPresenter.getZyPhotoList(),mPresenter.getTotalCount()),0);
    }
}
