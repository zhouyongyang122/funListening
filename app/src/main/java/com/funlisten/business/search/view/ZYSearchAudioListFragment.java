package com.funlisten.business.search.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.search.contract.ZYSearchContract;
import com.funlisten.business.search.model.bean.ZYAudioAndAlbumInfo;
import com.funlisten.business.search.view.viewholder.ZYSearchAudioItemVH;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.utils.ZYLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by gd on 2017/8/8.
 */

public class ZYSearchAudioListFragment extends ZYListDateFragment<ZYSearchContract.AudioPresenter ,ZYAudioAndAlbumInfo>
        implements ZYSearchAudioItemVH.AudioItemListener,ZYSearchContract.AudioView {

    ArrayList<ZYBaseViewHolder> viewHolders = new ArrayList<ZYBaseViewHolder>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }


    @Override
    protected void onItemClick(View view, int position) {

    }

    @Override
    protected ZYBaseViewHolder<ZYAudioAndAlbumInfo> createViewHolder() {
        return new ZYSearchAudioItemVH(this);
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
    public void refreshHeader(int totalCount) {

    }

    @Override
    public void loadAudio(ZYAlbumDetail albumDetail, ZYAudio audio) {
        ZYDownloadEntity downloadEntity = ZYDownloadEntity.createEntityByAudio(albumDetail, audio);
        ZYDownloadManager.getInstance().addAudio(downloadEntity);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            for (ZYBaseViewHolder viewHolder : viewHolders) {
                EventBus.getDefault().unregister(viewHolder);
                ZYLog.e(getClass().getSimpleName(), "onDestroyView-eventBus: ");
            }
        } catch (Exception e) {

        }
    }
}
