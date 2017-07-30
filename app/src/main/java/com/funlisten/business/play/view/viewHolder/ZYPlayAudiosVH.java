package com.funlisten.business.play.view.viewHolder;

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
        }
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
                    textPlayType.setSelected(true);
                    textPlayType.setText("随机");
                    ZYPlayManager.getInstance().setPlayType(ZYPlayService.PLAY_RANDOM_TYPE);
                } else {
                    textPlayType.setSelected(false);
                    textPlayType.setText("循环");
                    ZYPlayManager.getInstance().setPlayType(ZYPlayService.PLAY_LOOP_TYPE);
                }
                break;
            case R.id.textPlaySort:
                Collections.reverse(mAudios);
                ZYPlayManager.getInstance().setAudios(mAudios);
                break;
            case R.id.textClose:
                unAttachTo();
                break;
        }
    }

    public interface PlayAudiosListener {
        void onAudiosItemClick(int position);
    }
}
