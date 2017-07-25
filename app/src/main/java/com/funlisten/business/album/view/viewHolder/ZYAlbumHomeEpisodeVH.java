package com.funlisten.business.album.view.viewHolder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.funlisten.R;
import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.adapter.ZYCommonAdapter;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYAlbumEpisode;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/4.
 * 专辑主页 选集视图
 */

public class ZYAlbumHomeEpisodeVH extends ZYBaseViewHolder<ZYAlbumDetail> {

    ZYAlbumDetail mData;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    ZYBaseRecyclerAdapter<ZYAlbumEpisode> adapter;

    AlbumHomeEpisodeListener listener;

    public ZYAlbumHomeEpisodeVH(AlbumHomeEpisodeListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(ZYAlbumDetail data, int position) {
        if (data != null) {
            mData = data;
            if (adapter == null) {
                adapter = new ZYBaseRecyclerAdapter<ZYAlbumEpisode>() {
                    @Override
                    public ZYBaseViewHolder<ZYAlbumEpisode> createViewHolder(int type) {
                        return new ZYAlbumHomeEpisodeItemVH();
                    }
                };
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));

                int count = data.audioCount;
                ArrayList<ZYAlbumEpisode> episodes = new ArrayList<ZYAlbumEpisode>();
                int page = count / 20;
                int start = 0;
                for (int i = 0; i < page; i++) {
                    start = i * 20;
                    episodes.add(new ZYAlbumEpisode(start + 1, start + 20));
                }

                if (count % 20 > 0) {
                    episodes.add(new ZYAlbumEpisode(start + 1, start + count % 20));
                }

                adapter.setDatas(episodes);

                adapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ZYAlbumEpisode episode = adapter.getItem(position);
                        listener.onEpisodeItemClick(episode);
                    }
                });
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_album_episode;
    }

    public interface AlbumHomeEpisodeListener {
        void onEpisodeItemClick(ZYAlbumEpisode episode);
    }
}
