package com.funlisten.business.album.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumAudiosHeaderVH extends ZYBaseViewHolder<Object> {

    AlbumAudiosHeaderListener listener;

    @Bind(R.id.textEpisode)
    TextView textEpisode;

    ZYAlbumDetail albumDetail;

    public ZYAlbumAudiosHeaderVH(AlbumAudiosHeaderListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {
        if (data != null && data instanceof ZYAlbumDetail) {
            albumDetail = (ZYAlbumDetail) data;
        }

        if (albumDetail != null && textEpisode != null) {
            textEpisode.setText("已经更新到" + albumDetail.audioCount + "集");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_album_audios_header;
    }

    @OnClick({R.id.textSort, R.id.textEpisode, R.id.textChoice, R.id.textDown})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textDown:
                listener.onDownloadClick();
                break;
            case R.id.textEpisode:
                listener.onEpisodeClick();
                break;
            case R.id.textChoice:
                listener.onChoiceClick();
                break;
            case R.id.textSort:
                listener.onSortClick();
                break;
        }
    }

    public interface AlbumAudiosHeaderListener {
        void onSortClick();

        void onChoiceClick();

        void onEpisodeClick();

        void onDownloadClick();
    }
}
