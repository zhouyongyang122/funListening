package com.funlisten.business.album.view.viewHolder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumTitle;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumDetailTitleVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Override
    public void updateView(Object obj, int position) {
        if (obj != null && obj instanceof ZYAlbumTitle) {
            ZYAlbumTitle data = (ZYAlbumTitle) obj;
            textTitle.setText(data.title);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_album_detail_title;
    }
}
