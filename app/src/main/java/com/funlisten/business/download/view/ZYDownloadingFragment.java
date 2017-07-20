package com.funlisten.business.download.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.contract.ZYDownloadedContract;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.download.view.viewholder.ZYDownloadedItemVH;
import com.funlisten.business.download.view.viewholder.ZYDownloadingHeaderVH;
import com.funlisten.business.download.view.viewholder.ZYDownloadingItemVH;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadingFragment extends ZYListDateFragment<ZYDownloadedContract.IPresenter, ZYDownloadEntity> implements ZYDownloadedContract.IView{

    ZYDownloadingHeaderVH headerVH;

    @Override
    protected void onItemClick(View view, int position) {
        ZYDownloadEntity downloadEntity = mAdapter.getItem(position);
        ZYPlayActivity.toPlayActivity(mActivity, downloadEntity.albumId, downloadEntity.audioId);
    }

    @Override
    protected void init() {
        super.init();
        headerVH = new ZYDownloadingHeaderVH(new ZYDownloadingHeaderVH.DownloadingHeaderListener() {
            @Override
            public void onClearAllClick() {
                new AlertDialog.Builder(mActivity).setTitle("删除").setMessage("是否清除下载列表?")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ZYDownloadManager.getInstance().deleteAllAudio();
                                mPresenter.subscribe();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }

            @Override
            public void onDownAllClick(boolean isDowAll) {

            }
        });
        mAdapter.addHeader(headerVH);
        mRefreshRecyclerView.setLoadMoreEnable(false);
        mRefreshRecyclerView.setRefreshEnable(false);
    }

    @Override
    protected ZYBaseViewHolder<ZYDownloadEntity> createViewHolder() {
        return new ZYDownloadingItemVH();
    }
}
