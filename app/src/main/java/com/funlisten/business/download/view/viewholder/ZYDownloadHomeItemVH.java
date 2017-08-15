package com.funlisten.business.download.view.viewholder;

import android.text.TextUtils;
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

    final float SIZE_M = 1024 * 1204.0f;

    DownloadHomeItemListener mListener;

    public ZYDownloadHomeItemVH(DownloadHomeItemListener listener) {
        mListener = listener;
    }

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null && !TextUtils.isEmpty(data.id)) {
            mItemView.setVisibility(View.VISIBLE);
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, data.getAlbumDetail().coverUrl);
            textTitle.setText(mData.getAlbumDetail().name);
            textName.setText(mData.getAlbumDetail().publisher.nickname);
            textUpdateCount.setText("已经更新到" + mData.getAlbumDetail().audioCount + "集");
            textDownCount.setText("已经下载" + mData.audioDowloadedCount + "集");
            textSize.setText(String.format("%.2fM", ((float) mData.albumDownloadedSize / SIZE_M)));
        } else {
            mItemView.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.layoutDel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutDel:
                mListener.onDelClick(mData);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_download_home_item;
    }

    public interface DownloadHomeItemListener {
        void onDelClick(ZYDownloadEntity data);
    }

}
