package com.funlisten.business.dailylisten.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.mvp.ZYBaseRecyclerFragment;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.view.viewHolder.ZYAlbumDetailTitleVH;
import com.funlisten.business.dailylisten.activity.ZYDailyListenActivity;
import com.funlisten.business.dailylisten.contract.ZYDailyListenContract;
import com.funlisten.business.dailylisten.model.bean.ZYHeaderInfo;
import com.funlisten.business.dailylisten.model.bean.ZYTimeInfo;
import com.funlisten.business.dailylisten.view.viewholder.ZYDailyHeaderVH;
import com.funlisten.business.dailylisten.view.viewholder.ZYDailyListenVH;
import com.funlisten.business.dailylisten.view.viewholder.ZYDailyTimeVH;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYScreenUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by gd on 2017/7/20.
 */

public class ZYDailyFragment extends ZYBaseRecyclerFragment<ZYDailyListenContract.IPresenter> implements ZYDailyListenContract.IView,ZYDailyListenVH.AudioItemListener{
    static final int ADAPTER_TYPE_TITLE = 0;
    static final int ADAPTER_TYPE_DESC = 1;
    static final int ADAPTER_TYPE_HEAD = 2;
    ZYBaseRecyclerAdapter<Object> mAdapter;
    ArrayList<ZYBaseViewHolder> viewHolders = new ArrayList<ZYBaseViewHolder>();
    ZYDailyListenActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (ZYDailyListenActivity) getActivity();
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRefreshRecyclerView.setLoadMoreEnable(false);
        mRefreshRecyclerView.setRefreshEnable(false);
        mRefreshRecyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        int position = linearLayoutManager.findFirstVisibleItemPosition();
                        View current = linearLayoutManager.findViewByPosition(position);

                        int[] location = new int[2];
                        current.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
                        Log.e("recyclerview","position = "+position+"  location = "+location[1]);
                        if(position == 0 && location[1] > 0){
                            activity.showChangeBar(true);
                        }else if (position >= 0){
                            activity.showChangeBar(false);
                        }
                    }
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mAdapter = new ZYBaseRecyclerAdapter<Object>(mPresenter.getDataList()) {

            @Override
            public int getItemViewType(int position) {
                int type = super.getItemViewType(position);
                if (type == ZYBaseRecyclerAdapter.TYPE_NORMAL) {
                    if (getItem(position) instanceof ZYTimeInfo) {
                        return ADAPTER_TYPE_TITLE;
                    } else if (getItem(position) instanceof ZYAudio) {
                        return ADAPTER_TYPE_DESC;
                    }else if(getItem(position) instanceof ZYHeaderInfo){
                        return ADAPTER_TYPE_HEAD;
                    }
                }
                return type;
            }

            @Override
            public ZYBaseViewHolder<Object> createViewHolder(int type) {
                if (ADAPTER_TYPE_TITLE == type) {
                    return new ZYDailyTimeVH();
                } else if (ADAPTER_TYPE_DESC == type) {
                    return new ZYDailyListenVH(ZYDailyFragment.this);
                }else if(ADAPTER_TYPE_HEAD == type){
                    return new ZYDailyHeaderVH();
                }
                return new ZYAlbumDetailTitleVH();
            }
        };
        mAdapter.setOnItemClickListener(onItemClickListener);
        mRefreshRecyclerView.setAdapter(mAdapter);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRefreshRecyclerView.getLoadingView().getView().getLayoutParams();
        params.height = ZYScreenUtils.dp2px(mActivity, 160);
        params.topMargin = ZYScreenUtils.dp2px(mActivity, 40);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mRefreshRecyclerView.getLoadingView().getView().setLayoutParams(params);

        mRefreshRecyclerView.getLoadingView().setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribe();
            }
        });
        return  view;
    }

    ZYBaseRecyclerAdapter.OnItemClickListener onItemClickListener = new ZYBaseRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Object object =  mPresenter.getDataList().get(position);
            if(object instanceof ZYAudio ){
                ZYAudio audio = (ZYAudio) object;
                ZYPlayActivity.toPlayActivity(mActivity,audio.albumId,audio.id);
            }
        }
    };

    @Override
    public void showDatas(ArrayList<Object> data) {
        mAdapter.notifyDataSetChanged();
        showList(false);
    }

    @Override
    public void loadAudio(ZYAlbumDetail  albumDetail,ZYAudio audio) {
        ZYDownloadEntity downloadEntity = ZYDownloadEntity.createEntityByAudio(albumDetail, audio);
        ZYDownloadManager.getInstance().addAudio(downloadEntity);
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

