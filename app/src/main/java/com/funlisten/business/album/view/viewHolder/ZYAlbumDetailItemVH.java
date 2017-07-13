package com.funlisten.business.album.view.viewHolder;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.zzhoujay.richtext.RichText;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYAlbumDetailItemVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.textDetail)
    TextView textDetail;

    @Override
    public void updateView(Object obj, int position) {
        if (obj != null && obj instanceof ZYAlbumDetail.Detail) {
            ZYAlbumDetail.Detail data = (ZYAlbumDetail.Detail) obj;
            RichText.from(data.content).into(textDetail);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_album_detail_item;
    }
}
