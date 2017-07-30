package com.funlisten.business.mylike.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.favorite.ZYFavorite;
import com.funlisten.business.mylike.contract.ZYMyLikeContract;
import com.funlisten.business.mylike.model.bean.ZYMyLikeHeadInfo;
import com.funlisten.business.mylike.view.viewholder.ZYMyLikeHeader;
import com.funlisten.business.mylike.view.viewholder.ZYMyLikeItem;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYResourceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYMyLikeFragment extends ZYListDateFragment<ZYMyLikeContract.IPresenter,ZYFavorite> implements ZYMyLikeContract.IView , ZYMyLikeItem.AudioItemListener {

    ZYMyLikeHeader likeHeader;

    ArrayList<ZYBaseViewHolder> viewHolders = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.getRecyclerView().setBackgroundColor(ZYResourceUtils.getColor(R.color.c8));
        likeHeader = new ZYMyLikeHeader();
        mAdapter.addHeader(likeHeader);
        return view;
    }

    @Override
    public void refreshView(int totalCount) {
        mAdapter.notifyDataSetChanged();
        likeHeader.updateView(new ZYMyLikeHeadInfo(totalCount),0);
    }

    @Override
    public void loadAudio(ZYAlbumDetail albumDetail, ZYAudio audio) {
        ZYDownloadEntity downloadEntity = ZYDownloadEntity.createEntityByAudio(albumDetail, audio);
        ZYDownloadManager.getInstance().addAudio(downloadEntity);
    }

    @Override
    protected void onItemClick(View view, int position) {
        ZYFavorite data = mPresenter.getDataList().get(position);
        ZYPlayActivity.toPlayActivity(mActivity, data.audio.albumId, data.audio.id);
    }

    @Override
    protected ZYBaseViewHolder<ZYFavorite> createViewHolder() {
        return new ZYMyLikeItem(this);
    }


    @Override
    public ZYDownloadEntity onDownloadClick(ZYAudio audio) {
        mPresenter.getAlbumDetail(audio);
        return null;
    }

    @Override
    public void addEvents(ZYBaseViewHolder viewHolder) {
        viewHolders.add(viewHolder);
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
