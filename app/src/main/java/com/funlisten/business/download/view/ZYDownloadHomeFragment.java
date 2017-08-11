package com.funlisten.business.download.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ZYDownloadHomeFragment extends ZYListDateFragment<ZYDownloadHomeContract.IPresenter, ZYDownloadEntity> implements ZYDownloadHomeContract.IView, ZYDownloadHomeHeaderVH.DownloadHomeHeaderListener,
        ZYDownloadHomeItemVH.DownloadHomeItemListener {

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
        mActivity.startActivity(ZYDownloadedActivity.createIntent(mActivity, downloadEntity.albumId, downloadEntity.getAlbumDetail().name));
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
        return new ZYDownloadHomeItemVH(this);
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
    public void onDelClick(final ZYDownloadEntity data) {
        new AlertDialog.Builder(mActivity).setTitle("删除").setMessage("是否删除音频?")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ZYDownloadEntity.delByAlbumId(data.albumId);
                        mPresenter.getDataList().remove(data);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (homeHeaderVH != null) {
            homeHeaderVH.unAttachTo();
        }
    }
}
