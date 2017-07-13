package com.funlisten.business.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.activity.ZYAlbumHomeActivity;
import com.funlisten.business.album.contract.ZYAlbumListContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.view.viewHolder.ZYAlbumListItemVH;

/**
 * Created by ZY on 17/6/13.
 */

public class ZYAlbumListFragment extends ZYListDateFragment<ZYAlbumListContract.IPresenter, ZYAlbumDetail> implements ZYAlbumListContract.IView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mRefreshRecyclerView.getLoadingView().setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribe();
            }
        });

        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {
        ZYAlbumDetail ablum = mAdapter.getItem(position);
        if (ablum != null) {
            //跳转专辑详情
            mActivity.startActivity(ZYAlbumHomeActivity.createIntent(mActivity, ablum.id));
        }
    }

    @Override
    protected ZYBaseViewHolder<ZYAlbumDetail> createViewHolder() {
        return new ZYAlbumListItemVH();
    }
}
