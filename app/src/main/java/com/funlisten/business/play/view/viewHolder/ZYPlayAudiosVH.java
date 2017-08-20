package com.funlisten.business.play.view.viewHolder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.view.ZYRefreshListener;
import com.funlisten.base.view.ZYSwipeRefreshRecyclerView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.play.ZYPlayService;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.business.play.model.ZYPlayModel;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayAudiosVH extends ZYBaseViewHolder<List<ZYAudio>> {

    @Bind(R.id.textPlayType)
    TextView textPlayType;

    @Bind(R.id.textPlaySort)
    TextView textPlaySort;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.textClose)
    TextView textClose;

    List<ZYAudio> mAudios;

    int mPosition;

    ZYBaseRecyclerAdapter<ZYAudio> adapter;

    PlayAudiosListener listener;

    String mSortType = ZYBaseModel.SORT_DESC;

    public ZYPlayAudiosVH(PlayAudiosListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(List<ZYAudio> data, int position) {
        if (data != null) {
            show();
            mAudios = data;
            mPosition = position;
            adapter = new ZYBaseRecyclerAdapter<ZYAudio>(mAudios) {
                @Override
                public ZYBaseViewHolder<ZYAudio> createViewHolder(int type) {
                    return new ZYPlayAudiosItemVH();
                }
            };
            adapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    hide();
                    listener.onAudiosItemClick(position);
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

            refreshPlayType();

            refreshSortType();
        }
    }

    void refreshSortType() {
        if (textPlaySort == null) {
            return;
        }
        if (mSortType.equals(ZYBaseModel.SORT_DESC)) {
            textPlaySort.setSelected(false);
        } else {
            textPlaySort.setSelected(true);
        }
    }

    void refreshPlayType() {
        Drawable drawable = null;
        if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_LOOP_TYPE) {
            drawable = mContext.getResources().getDrawable(R.drawable.btn_change_or_cycle_n);
            textPlayType.setText("顺序");
        } else if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_SINTANCE_TYPE) {
            textPlayType.setText("单曲");
            drawable = mContext.getResources().getDrawable(R.drawable.icon_single_cycle);
        } else {
            textPlayType.setText("随机");
            drawable = mContext.getResources().getDrawable(R.drawable.icon_shuffle_playback);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textPlayType.setCompoundDrawables(null, drawable, null, null);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_play_audios;
    }

    @OnClick({R.id.textPlayType, R.id.textPlaySort, R.id.textClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textPlayType:
                if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_LOOP_TYPE) {
                    ZYPlayManager.getInstance().setPlayType(ZYPlayService.PLAY_RANDOM_TYPE);
                } else if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_RANDOM_TYPE) {
                    ZYPlayManager.getInstance().setPlayType(ZYPlayService.PLAY_SINTANCE_TYPE);
                } else {
                    ZYPlayManager.getInstance().setPlayType(ZYPlayService.PLAY_LOOP_TYPE);
                }
                refreshPlayType();
                break;
            case R.id.textPlaySort:
                Collections.reverse(mAudios);
                ZYPlayManager.getInstance().setAudios(mAudios);
                adapter.notifyDataSetChanged();

                if (mSortType.equals(ZYBaseModel.SORT_DESC)) {
                    mSortType = ZYBaseModel.SORT_ASC;
                } else {
                    mSortType = ZYBaseModel.SORT_DESC;
                }
                refreshSortType();
                break;
            case R.id.textClose:
                hide();
                listener.onAudiosViewClose();
                break;
        }
    }

    public interface PlayAudiosListener {
        void onAudiosItemClick(int position);

        void onAudiosViewClose();
    }
}
