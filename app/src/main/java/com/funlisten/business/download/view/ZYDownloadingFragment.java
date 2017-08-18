package com.funlisten.business.download.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.contract.ZYDownloadedContract;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.download.view.viewholder.ZYDownloadingHeaderVH;
import com.funlisten.business.download.view.viewholder.ZYDownloadingItemVH;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.service.downNet.down.ZYDownState;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.utils.ZYLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadingFragment extends ZYListDateFragment<ZYDownloadedContract.IPresenter, ZYDownloadEntity> implements ZYDownloadedContract.IView
        , ZYDownloadingItemVH.DownloadingItemListener {

    ZYDownloadingHeaderVH headerVH;

    ArrayList<ZYBaseViewHolder> viewHolders = new ArrayList<ZYBaseViewHolder>();

    @Override
    protected void onItemClick(View view, int position) {
        ZYDownloadEntity downloadEntity = mAdapter.getItem(position);
        if (downloadEntity.getState() == ZYDownState.FINISH) {
            return;
        }
        if (downloadEntity.getState() == ZYDownState.PAUSE || downloadEntity.getState() == ZYDownState.ERROR) {
            downloadEntity.stateValue = ZYDownState.WAIT.getState();
            ZYDownloadManager.getInstance().cancleAudio(downloadEntity.getId());
        } else {
            downloadEntity.stateValue = ZYDownState.PAUSE.getState();
            ZYDownloadManager.getInstance().addAudio(downloadEntity);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshDownloadAllState(boolean hasPaseedEntity) {
        headerVH.refreshDownloadAllState(hasPaseedEntity);
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
                if (isDowAll) {
                    ZYDownloadManager.getInstance().startAllAudioAsy();
                } else {
                    ZYDownloadManager.getInstance().puaseAllAudioAsy();
                }
                mPresenter.refresh();
            }
        });
        mAdapter.addHeader(headerVH);
        mRefreshRecyclerView.setLoadMoreEnable(false);
        mRefreshRecyclerView.setRefreshEnable(false);
    }

    @Override
    protected ZYBaseViewHolder<ZYDownloadEntity> createViewHolder() {
        return new ZYDownloadingItemVH(this);
    }

    @Override
    public void addEvents(ZYBaseViewHolder viewHolder) {
        viewHolders.add(viewHolder);
    }

    @Override
    public void downloadFinished(ZYDownloadEntity data) {
        mPresenter.getDataList().remove(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            for (ZYBaseViewHolder viewHolder : viewHolders) {
                EventBus.getDefault().unregister(viewHolder);
                ZYLog.e(getClass().getSimpleName(), "onDestroyView-eventBus: ");
            }
        } catch (Exception e) {

        }
    }
}
