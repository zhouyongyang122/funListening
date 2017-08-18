package com.funlisten.business.album.view.viewHolder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.ZYAlbumModel;
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

    @Bind(R.id.textSort)
    TextView textSort;

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

    public void refreshSortView(String sortTye){
        if (sortTye == ZYAlbumModel.SORT_ASC) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.btn_reverse_order_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textSort.setCompoundDrawables(drawable, null, null, null);
            textSort.setText("正序");
        } else {
            Drawable drawable =  mContext.getResources().getDrawable(R.drawable.btn_sorting_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textSort.setCompoundDrawables(drawable, null, null, null);
            textSort.setText("倒序");
        }
    }

    public interface AlbumAudiosHeaderListener {
        void onSortClick();

        void onChoiceClick();

        void onEpisodeClick();

        void onDownloadClick();
    }
}
