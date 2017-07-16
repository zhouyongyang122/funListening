package com.funlisten.business.download.view;

import android.view.View;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.contract.ZYDownloadHomeContract;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.download.view.viewholder.ZYDownloadedHeaderVH;
import com.funlisten.business.download.view.viewholder.ZYDownloadedItemVH;
import com.funlisten.business.play.activity.ZYPlayActivity;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadedFragment extends ZYListDateFragment<ZYDownloadHomeContract.IPresenter, ZYDownloadEntity> implements ZYDownloadHomeContract.IView, ZYDownloadedItemVH.DownloadedItemListener {

    ZYDownloadedHeaderVH headerVH;

    @Override
    protected void onItemClick(View view, int position) {
        ZYDownloadEntity downloadEntity = mAdapter.getItem(position);
        ZYPlayActivity.toPlayActivity(mActivity, downloadEntity.albumId, downloadEntity.audioId);
    }

    @Override
    protected void init() {
        super.init();
        headerVH = new ZYDownloadedHeaderVH();
        mAdapter.addHeader(headerVH);
    }

    @Override
    protected ZYBaseViewHolder<ZYDownloadEntity> createViewHolder() {
        return new ZYDownloadedItemVH(this);
    }

    @Override
    public void refresh(Object object) {
        ZYDownloadEntity entity = (ZYDownloadEntity) object;
        entity.audioDowloadedCount = mPresenter.getDataList().size();
        headerVH.updateView(entity, 0);
    }

    @Override
    public void onItemDelClick(ZYDownloadEntity data) {
        data.delete();
        mPresenter.getDataList().remove(data);
    }
}
