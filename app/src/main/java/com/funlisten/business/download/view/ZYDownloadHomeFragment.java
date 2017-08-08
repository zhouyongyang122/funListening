package com.funlisten.business.download.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.activity.ZYDownloadedActivity;
import com.funlisten.business.download.contract.ZYDownloadHomeContract;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.download.view.viewholder.ZYDownloadHomeHeaderVH;
import com.funlisten.business.download.view.viewholder.ZYDownloadHomeItemVH;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYDownloadHomeFragment extends ZYListDateFragment<ZYDownloadHomeContract.IPresenter, ZYDownloadEntity> implements ZYDownloadHomeContract.IView, ZYDownloadHomeHeaderVH.DownloadHomeHeaderListener {

    ZYDownloadHomeHeaderVH homeHeaderVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.setRefreshEnable(false);
        mRefreshRecyclerView.setLoadMoreEnable(false);
        return view;
    }

    @Override
    protected void init() {
        super.init();
        homeHeaderVH = new ZYDownloadHomeHeaderVH(this);
        mAdapter.addHeader(homeHeaderVH);
    }

    @Override
    protected void onItemClick(View view, int position) {
        ZYDownloadEntity downloadEntity = mAdapter.getItem(position);
        mActivity.startActivity(ZYDownloadedActivity.createIntent(mActivity, downloadEntity.albumId, downloadEntity.albumName));
    }

    @Override
    public void refresh(Object object) {
        if (object == null) {
            homeHeaderVH.hideView(null);
            return;
        }
        homeHeaderVH.updateView((ZYDownloadEntity) object, 0);
    }

    @Override
    protected ZYBaseViewHolder<ZYDownloadEntity> createViewHolder() {
        return new ZYDownloadHomeItemVH();
    }

    @Override
    public void onDownloadAllFinished() {
        mPresenter.subscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (homeHeaderVH != null) {
            homeHeaderVH.unAttachTo();
        }
    }
}
