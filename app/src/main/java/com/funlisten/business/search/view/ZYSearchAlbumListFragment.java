package com.funlisten.business.search.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.profile.view.viewholder.ZYProfileVH;
import com.funlisten.business.search.contract.ZYSearchContract;
import com.funlisten.business.search.model.bean.ZYAudioAndAlbumInfo;
import com.funlisten.business.search.view.viewholder.ZYSearchAlbumVH;
import com.funlisten.utils.ZYResourceUtils;
import com.funlisten.utils.ZYScreenUtils;

/**
 * Created by gd on 2017/8/8.
 */

public class ZYSearchAlbumListFragment extends ZYListDateFragment<ZYSearchContract.AlbumPresenter,ZYAudioAndAlbumInfo> implements ZYSearchContract.AlbumView {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        int space = ZYScreenUtils.dp2px(mActivity, 10);
        mRefreshRecyclerView.getRecyclerView().setPadding(space, 0, 0, 0);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c8));
        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected ZYBaseViewHolder<ZYAudioAndAlbumInfo> createViewHolder() {
        return new ZYSearchAlbumVH();
    }

    @Override
    public void refreshHeader(int totalCount) {

    }
}
