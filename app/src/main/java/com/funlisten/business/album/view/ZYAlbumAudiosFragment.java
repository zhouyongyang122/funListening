package com.funlisten.business.album.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.funlisten.base.mvp.ZYListDateFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.contract.ZYAlbumAudiosContract;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYAlbumEpisode;
import com.funlisten.business.album.view.viewHolder.ZYAlbumAudiosHeaderVH;
import com.funlisten.business.album.view.viewHolder.ZYAlbumHomeEpisodeVH;
import com.funlisten.business.album.view.viewHolder.ZYAudioItemVH;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYScreenUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumAudiosFragment extends ZYListDateFragment<ZYAlbumAudiosContract.IPresenter, ZYAudio> implements ZYAlbumAudiosContract.IView, ZYAudioItemVH.AudioItemListener, ZYAlbumAudiosHeaderVH.AlbumAudiosHeaderListener, ZYAlbumHomeEpisodeVH.AlbumHomeEpisodeListener {

    ZYAlbumAudiosHeaderVH homeHeaderVH;

    ZYAlbumHomeEpisodeVH episodeVH;

    ZYAlbumDetail albumDetail;

    ArrayList<ZYBaseViewHolder> viewHolders = new ArrayList<ZYBaseViewHolder>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        homeHeaderVH = new ZYAlbumAudiosHeaderVH(this);
        mAdapter.addHeader(homeHeaderVH);

        episodeVH = new ZYAlbumHomeEpisodeVH(this);
        episodeVH.attachTo(mRootView);
        episodeVH.hide();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRefreshRecyclerView.getLoadingView().getView().getLayoutParams();
        params.height = ZYScreenUtils.dp2px(mActivity, 160);
        params.topMargin = ZYScreenUtils.dp2px(mActivity, 40);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mRefreshRecyclerView.getLoadingView().getView().setLayoutParams(params);

        mRefreshRecyclerView.setRefreshEnable(false);

        return view;
    }

    @Override
    protected void onItemClick(View view, int position) {
        ZYAudio data = mAdapter.getItem(position);
        ZYPlayActivity.toPlayActivity(mActivity, albumDetail.id, data.id,mPresenter.getSortType());
    }

    public void setAlbumDetail(ZYAlbumDetail albumDetail) {
        this.albumDetail = albumDetail;
    }

    @Override
    protected ZYBaseViewHolder<ZYAudio> createViewHolder() {
        return new ZYAudioItemVH(this);
    }

    @Override
    public void onSortClick() {
        mPresenter.changerSortType();
    }

    @Override
    public void onChoiceClick() {
        episodeVH.updateView(albumDetail, 0);
        episodeVH.show();
    }

    @Override
    public void onDownloadClick() {

    }

    @Override
    public void onEpisodeItemClick(ZYAlbumEpisode episode) {
        episodeVH.hide();
        mPresenter.choiceEpisode(episode.start);
    }

    @Override
    public ZYDownloadEntity onDownloadClick(ZYAudio audio) {
        ZYDownloadEntity downloadEntity = ZYDownloadEntity.createEntityByAudio(albumDetail, audio);
        ZYDownloadManager.getInstance().addAudio(downloadEntity);
        return downloadEntity;
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
