package com.funlisten.business.pay.view.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYToast;

import butterknife.Bind;

/**
 * Created by gd on 2017/7/29.
 */

public class ZYPayHeaderDetails extends ZYBaseViewHolder<ZYAlbumDetail> {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textCate)
    TextView textCate;

    @Bind(R.id.textAnchor)
    TextView textAnchor;

    @Bind(R.id.textPlayNum)
    TextView textPlayNum;

    ZYAlbumDetail mData;

    @Override
    public void updateView(ZYAlbumDetail data, int position) {
        if (data != null) {
            mData = data;
            ZYLog.e("costType = "+mData.costType);
            textTitle.setText(mData.name);
            textCate.setText("类别: " + mData.getCategoryNames());
            textAnchor.setText("主播: " + mData.publisher.nickname);
            textPlayNum.setText("播放: " + mData.playCount);

            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, mData.coverUrl);
        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_album_details_hd_item;
    }
}
