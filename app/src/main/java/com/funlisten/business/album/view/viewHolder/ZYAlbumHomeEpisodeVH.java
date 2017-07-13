package com.funlisten.business.album.view.viewHolder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.funlisten.R;
import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.adapter.ZYBaseRecyclerAdapter;
import com.funlisten.base.adapter.ZYCommonAdapter;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYAlbumEpisode;

import java.util.ArrayList;

/**
 * Created by ZY on 17/7/4.
 * 专辑主页 选集视图
 */

public class ZYAlbumHomeEpisodeVH extends ZYBaseViewHolder<ZYAlbumDetail> {

    ZYAlbumDetail mData;

    RecyclerView recyclerView;

    ZYBaseRecyclerAdapter<ZYAlbumEpisode> adapter;

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
                int start = 0;
                int end = 0;
                for (int i = 0; i < count; i++) {
                    if ((end % 19 == 0) || (i == count - 1)) {
                        episodes.add(new ZYAlbumEpisode(start, end));
                        start = end;
                    }
                    end++;
                }

                adapter.setDatas(episodes);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_album_episode;
    }
}
