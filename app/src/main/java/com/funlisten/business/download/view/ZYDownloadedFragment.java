package com.funlisten.business.download.view;

import android.view.View;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.contract.ZYDownloadHomeContract;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.download.view.viewholder.ZYDownloadedHeaderVH;
import com.funlisten.business.download.view.viewholder.ZYDownloadedItemVH;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadedFragment extends ZYListDateFragment<ZYDownloadHomeContract.IPresenter, ZYDownloadEntity> implements ZYDownloadHomeContract.IView {

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected void init() {
        super.init();
        mAdapter.addHeader(new ZYDownloadedHeaderVH());
    }

    @Override
    protected ZYBaseViewHolder<ZYDownloadEntity> createViewHolder() {
        return new ZYDownloadedItemVH();
    }
}
