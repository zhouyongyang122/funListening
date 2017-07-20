package com.funlisten.business.download.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadHomeItemVH extends ZYBaseViewHolder<ZYDownloadEntity> {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textUpdateCount)
    TextView textUpdateCount;

    @Bind(R.id.textDownCount)
    TextView textDownCount;

    @Bind(R.id.textSize)
    TextView textSize;

    @Bind(R.id.layoutDel)
    RelativeLayout layoutDel;

    ZYDownloadEntity mData;

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null) {
            mItemView.setVisibility(View.VISIBLE);
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, data.albumCoverUrl);
            textTitle.setText(mData.albumName);
            textName.setText(mData.albumPublisher);
            textUpdateCount.setText("已经更新到" + mData.audioUpatedCount + "集");
            textDownCount.setText("已经下载" + mData.audioDowloadedCount + "集");
            textSize.setText(String.format("%.2f", ((float) mData.albumDownloadedSize / 1024.0f)));
        } else {
            mItemView.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.layoutDel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutDel:
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_download_home_item;
    }
}
