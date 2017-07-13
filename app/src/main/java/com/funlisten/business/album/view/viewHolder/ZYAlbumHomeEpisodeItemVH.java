package com.funlisten.business.album.view.viewHolder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumEpisode;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/4.
 * 专辑主页 选集item
 */

public class ZYAlbumHomeEpisodeItemVH extends ZYBaseViewHolder<ZYAlbumEpisode> {

    @Bind(R.id.textValue)
    TextView textValue;

    ZYAlbumEpisode mData;

    @Override
    public void updateView(ZYAlbumEpisode data, int position) {
        if (data != null) {
            mData = data;
            textValue.setText(mData.start + " ~" + mData.end + "集");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_album_episode_item;
    }
}
