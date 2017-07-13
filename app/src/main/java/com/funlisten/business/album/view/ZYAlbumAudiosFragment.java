package com.funlisten.business.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.contract.ZYAlbumAudiosContract;
import com.funlisten.business.album.view.viewHolder.ZYAlbumAudiosHeaderVH;
import com.funlisten.business.album.view.viewHolder.ZYAudioItemVH;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.utils.ZYScreenUtils;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumAudiosFragment extends ZYListDateFragment<ZYAlbumAudiosContract.IPresenter, ZYAudio> implements ZYAlbumAudiosContract.IView {

    ZYAlbumAudiosHeaderVH homeHeaderVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        homeHeaderVH = new ZYAlbumAudiosHeaderVH();
        mAdapter.addHeader(homeHeaderVH);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRefreshRecyclerView.getLoadingView().getView().getLayoutParams();
        params.height = ZYScreenUtils.dp2px(mActivity, 160);
        params.topMargin = ZYScreenUtils.dp2px(mActivity, 40);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mRefreshRecyclerView.getLoadingView().getView().setLayoutParams(params);

        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected ZYBaseViewHolder<ZYAudio> createViewHolder() {
        return new ZYAudioItemVH();
    }
}
