package com.funlisten.business.play.view.viewHolder;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.view.ZYRefreshListener;
import com.funlisten.base.view.ZYSwipeRefreshRecyclerView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.play.model.ZYPlayModel;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayAudiosVH extends ZYBaseViewHolder<ZYAudio> {

    @Bind(R.id.textPlayType)
    TextView textPlayType;

    @Bind(R.id.textPlaySort)
    TextView textPlaySort;

    @Bind(R.id.recyclerView)
    ZYSwipeRefreshRecyclerView recyclerView;

    @Bind(R.id.textClose)
    TextView textClose;

    List<ZYAudio> mAudios;

    ZYAudio currentPlayAudio;

    ZYBaseRecyclerAdapter<ZYAudio> adapter;

    CompositeSubscription mSubscriptions = null;

    ZYPlayModel mModel = null;

    int mStart;

    int mRows = 20;

    int mEnd;

    String mSort = ZYAlbumModel.SORT_ASC;

    @Override
    public void updateView(ZYAudio data, int position) {
        if (data != null) {
            currentPlayAudio = data;
            mStart = currentPlayAudio.sort;
            mEnd = currentPlayAudio.sort;
            if (adapter == null) {
                adapter = new ZYBaseRecyclerAdapter<ZYAudio>(mAudios) {
                    @Override
                    public ZYBaseViewHolder<ZYAudio> createViewHolder(int type) {
                        return new ZYPlayAudiosItemVH();
                    }
                };
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setRefreshListener(new ZYRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadData(false);
                    }

                    @Override
                    public void onLoadMore() {
                        loadData(true);
                    }
                });
            }
        }
    }

    private void loadData(final boolean loadMore) {
        int pageIndex = 0;
        if (loadMore) {
            pageIndex = getPageIndex(mStart);
        } else {
            pageIndex = getPageIndex(mEnd);
        }

        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getAudios(pageIndex, mRows, currentPlayAudio.albumId, mSort),
                new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYAudio>>>() {
                    @Override
                    public void onSuccess(ZYResponse<ZYListResponse<ZYAudio>> response) {
                        super.onSuccess(response);
                        List<ZYAudio> audios = response.data.data;
                        if (audios != null && audios.size() > 0) {
                            if (loadMore) {
                                mEnd = audios.get(audios.size() - 1).sort;
                                mAudios.addAll(audios);
                            } else {
                                mStart = audios.get(0).sort;
                                mAudios.addAll(0, audios);
                                recyclerView.setEnabled(true);
                            }
                            recyclerView.showList(true);
                        } else {
                            if (loadMore) {
                                recyclerView.showList(false);
                            } else {
                                recyclerView.setRefreshEnable(false);
                            }
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        super.onFail(message);
                    }
                }));
    }

    private int getPageIndex(int index) {
        int pageIndex = index / mRows;
        if (index % mRows > 0) {
            pageIndex++;
        }
        return pageIndex;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_play_audios;
    }

    @OnClick({R.id.textPlayType, R.id.textPlaySort, R.id.textClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textPlayType:
                break;
            case R.id.textPlaySort:
                mStart = currentPlayAudio.sort;
                mEnd = currentPlayAudio.sort;
                mAudios.clear();
                loadData(false);
                break;
            case R.id.textClose:
                unAttachTo();
                break;
        }
    }

    @Override
    public void attachTo(ViewGroup viewGroup) {
        super.attachTo(viewGroup);
        mSubscriptions = new CompositeSubscription();
        mModel = new ZYPlayModel();
        mAudios = new ArrayList<ZYAudio>();
    }

    @Override
    public void unAttachTo() {
        super.unAttachTo();
        mSubscriptions.unsubscribe();
    }
}
