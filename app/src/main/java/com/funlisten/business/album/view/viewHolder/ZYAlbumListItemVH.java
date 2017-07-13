package com.funlisten.business.album.view.viewHolder;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;

import butterknife.Bind;

/**
 * Created by ZY on 17/6/16.
 */

public class ZYAlbumListItemVH extends ZYBaseViewHolder<ZYAlbumDetail> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textPayTag)
    TextView textPayTag;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textDesc)
    TextView textDesc;

    @Bind(R.id.textPlayNum)
    TextView textPlayNum;

    @Bind(R.id.textFreshNum)
    TextView textFreshNum;

    @Bind(R.id.textPrice)
    TextView textPrice;

    @Bind(R.id.textOPrice)
    TextView textOPrice;

    ZYAlbumDetail mData;

    @Override
    public void updateView(ZYAlbumDetail data, int position) {
        if (data != null) {
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, data.coverUrl, R.color.c1, R.color.c1);
            if (data.originPrice > 0) {
                textOPrice.setVisibility(View.VISIBLE);
                textOPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                textOPrice.setText("¥" + data.originPrice);
            } else {
                textOPrice.setVisibility(View.GONE);
            }
            if (data.sellPrice > 0) {
                textPayTag.setVisibility(View.VISIBLE);
            } else {
                textPayTag.setVisibility(View.GONE);
            }

            textTitle.setText(data.title);
            textDesc.setText(data.title);
            textPlayNum.setText(data.playCount + "次");
            textPrice.setText("¥" + data.sellPrice);
            textFreshNum.setText("更新至" + data.audioCount + "集");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_ablum_list_item;
    }
}
