package com.funlisten.business.download.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.utils.ZYDateUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadedItemVH extends ZYBaseViewHolder<ZYDownloadEntity> {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textSize)
    TextView textSize;

    @Bind(R.id.textDay)
    TextView textDay;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.imgDel)
    ImageView imgDel;

    ZYDownloadEntity mData;

    DownloadedItemListener listener;

    public ZYDownloadedItemVH(DownloadedItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null) {
            mData = data;
            textName.setText("第 " + data.audioSort + " 期 | " + mData.audioName);
            textSize.setText(String.format("%.2fM", ((float) mData.total / 1204.0f)));
            textDay.setText(ZYDateUtils.getTimeString(mData.audioCreateTime, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.YYMMDDHH));
            textTime.setText(ZYDateUtils.getTimeString(mData.audioCreateTime, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.HHMM24));

        }
    }

    @OnClick({R.id.imgDel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgDel:
                listener.onItemDelClick(mData);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_downloaded_item;
    }

    public interface DownloadedItemListener {
        void onItemDelClick(ZYDownloadEntity data);
    }
}
