package com.funlisten.business.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.mvp.ZYBaseRecyclerFragment;
import com.funlisten.base.view.ZYSwipeRefreshRecyclerView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYAlbumEpisode;
import com.funlisten.business.album.model.bean.ZYBatchDownHeaderInfo;
import com.funlisten.business.album.view.viewHolder.ZYAlbumHomeEpisodeVH;
import com.funlisten.business.album.view.viewHolder.ZYBatchDownFooterVH;
import com.funlisten.business.album.view.viewHolder.ZYBatchDownHeaderVH;
import com.funlisten.business.album.view.viewHolder.ZYBatchDownloadItemVH;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.utils.ZYScreenUtils;
import com.funlisten.utils.ZYToast;

import java.util.ArrayList;

/**
 * Created by gd on 2017/7/26.
 */

public class ZYBatchDownloadFragment extends ZYBaseRecyclerFragment implements
        ZYBatchDownloadItemVH.OnItemSelectAudio ,ZYBatchDownFooterVH.AllSelectListen,ZYAlbumHomeEpisodeVH.AlbumHomeEpisodeListener,
        ZYBatchDownHeaderVH.AudioSelectionsListener {

    protected ZYBaseRecyclerAdapter mAdapter;

    ArrayList<ZYAudio> selectList = new ArrayList<>();

    ZYBatchDownHeaderVH headerVH;

    ZYBatchDownFooterVH footerVH;

    ZYAlbumHomeEpisodeVH episodeVH;

    ZYAlbumDetail albumDetail = new ZYAlbumDetail();

    ArrayList<ZYAudio> noList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        init((ArrayList<ZYAudio>)bundle.getSerializable("audiolist"));
        footerVH = new ZYBatchDownFooterVH(this);
        footerVH.attachTo(mRootView);
        footerVH.updateView(null,0);

        episodeVH = new ZYAlbumHomeEpisodeVH(this);
        episodeVH.attachTo(mRootView);
        episodeVH.hide();

        return view;
    }

    public void init(ArrayList<ZYAudio> list){
        for(ZYAudio audio:list){
            if(!ZYDownloadEntity.audioIsDown(audio)) noList.add(audio);
        }
        mAdapter  = createAdapter(noList);
        albumDetail.audioCount = noList.size();
        mRefreshRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mActivity));
        mRefreshRecyclerView.getRecyclerView().setAdapter(mAdapter);
        headerVH = new ZYBatchDownHeaderVH(this);
        mAdapter.addHeader(headerVH);
        headerVH.updateView(new ZYBatchDownHeaderInfo(noList.size()),0);
        mRefreshRecyclerView.setRefreshEnable(false);
        ZYSwipeRefreshRecyclerView refreshRecyclerView = (ZYSwipeRefreshRecyclerView)mRefreshRecyclerView;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)refreshRecyclerView.getLayoutParams();
        layoutParams.bottomMargin = ZYScreenUtils.dp2px(mActivity,50);
        refreshRecyclerView.setLayoutParams(layoutParams);
        showList(false);
    }

    protected ZYBaseRecyclerAdapter createAdapter(ArrayList<ZYAudio> list) {
        return new ZYBaseRecyclerAdapter(list) {
            @Override
            public ZYBaseViewHolder createViewHolder(int type) {
                return ZYBatchDownloadFragment.this.createViewHolder();
            }
        };
    }

    protected  ZYBaseViewHolder createViewHolder(){
        return new ZYBatchDownloadItemVH(this);
    }

    @Override
    public void onSelect(ZYAudio audio) {
        if(selectList.contains(audio)){
            audio.isSelect =false;
            selectList.remove(audio);
            footerVH.isSelectAll(false);
        } else {
            audio.isSelect = true;
            selectList.add(audio);
        }
    }

    @Override
    public void onSelectAll(boolean isSelectAll) {
        for(ZYAudio audio:noList)audio.isSelect = isSelectAll;
        if(isSelectAll){
            selectList.clear();
            selectList.addAll(noList);
        }else{
            selectList.clear();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAllDown() {
        if(selectList.size() <=0 ){
            ZYToast.show(mActivity,"请选择下载项");
            return;
        }

        mActivity.finish();
    }

    @Override
    public void onEpisodeItemClick(ZYAlbumEpisode episode) {
        episodeVH.hide();
        mRefreshRecyclerView.getRecyclerView().scrollToPosition(episode.start);
    }

    @Override
    public void onSelections() {
        episodeVH.updateView(albumDetail, 0);
        episodeVH.show();
    }
}
