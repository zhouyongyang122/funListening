package com.funlisten.business.download.view.viewholder;

import android.view.View;
import android.widget.ImageView;
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

public class ZYDownloadedHeaderVH extends ZYBaseViewHolder<ZYDownloadEntity> {

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

    ZYDownloadEntity mData;

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null) {
            mData = data;
        }
        if (mData != null && textDownCount != null) {
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, mData.albumCoverUrl);
            textTitle.setText(mData.albumName);
            textName.setText(mData.albumPublisher);
            textUpdateCount.setText("已经更新到" + mData.audioUpatedCount + "集");
            textDownCount.setText("已经下载" + mData.audioDowloadedCount + "集");
            textSize.setText(String.format("%.2f", ((float) mData.albumDownloadedSize / 1024.0f)));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_downloaded_header;
    }

    @OnClick({R.id.textSort})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textSort:
                break;
        }
    }
}
