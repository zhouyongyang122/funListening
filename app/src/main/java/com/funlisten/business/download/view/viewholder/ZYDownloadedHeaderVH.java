package com.funlisten.business.download.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseModel;
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

    @Bind(R.id.textSort)
    TextView textSort;

    ZYDownloadEntity mData;

    String mSortType = ZYBaseModel.SORT_ASC;

    final float SIZE_M = 1024 * 1204.0f;

    DownloadedHeaderListener mListener;

    public ZYDownloadedHeaderVH(DownloadedHeaderListener listener) {
        mListener = listener;
    }

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null) {
            mData = data;
        }
        if (mData != null && textDownCount != null) {
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, mData.getAlbumDetail().coverUrl);
            textTitle.setText(mData.getAlbumDetail().name);
            textName.setText(mData.getAlbumDetail().publisher.nickname);
            textUpdateCount.setText("已经更新到" + mData.getAlbumDetail().audioCount + "集");
            textDownCount.setText("已经下载" + mData.audioDowloadedCount + "集");
            textSize.setText(String.format("%.2fM", ((float) mData.albumDownloadedSize / SIZE_M)));
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
                if (mSortType.equals(ZYBaseModel.SORT_ASC)) {
                    mSortType = ZYBaseModel.SORT_DESC;
                    textSort.setSelected(true);
                    textSort.setText("倒序");
                } else {
                    mSortType = ZYBaseModel.SORT_ASC;
                    textSort.setSelected(false);
                    textSort.setText("正序");
                }
                mListener.onSortClick(mSortType);
                break;
        }
    }

    public interface DownloadedHeaderListener {
        void onSortClick(String sortType);
    }
}
