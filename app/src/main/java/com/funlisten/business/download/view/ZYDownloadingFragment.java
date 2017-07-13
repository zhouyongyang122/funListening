package com.funlisten.business.download.view;

import android.view.View;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.contract.ZYDownloadedContract;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.download.view.viewholder.ZYDownloadedItemVH;
import com.funlisten.business.download.view.viewholder.ZYDownloadingHeaderVH;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadingFragment extends ZYListDateFragment<ZYDownloadedContract.IPresenter, ZYDownloadEntity> implements ZYDownloadedContract.IView {

    ZYDownloadingHeaderVH headerVH;

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected void init() {
        super.init();
        headerVH = new ZYDownloadingHeaderVH();
        mAdapter.addHeader(headerVH);
    }

    @Override
    protected ZYBaseViewHolder<ZYDownloadEntity> createViewHolder() {
        return new ZYDownloadedItemVH();
    }
}
