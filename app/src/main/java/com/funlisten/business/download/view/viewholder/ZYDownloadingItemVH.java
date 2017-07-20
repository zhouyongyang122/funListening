package com.funlisten.business.download.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.utils.ZYDateUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/20.
 */

public class ZYDownloadingItemVH extends ZYBaseViewHolder<ZYDownloadEntity> {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textSize)
    TextView textSize;

    @Bind(R.id.textMsg)
    TextView textMsg;

    ZYDownloadEntity mData;

    final float SIZE_M = 1024 * 1204.0f;

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null) {
            mData = data;
            textName.setText("第 " + data.audioSort + " 期 | " + mData.audioName);
            refresh();
        }
    }

    private void refresh() {
        textSize.setText(String.format("%.2fM", ((float) mData.current / SIZE_M)) + "/" + String.format("%.2fM", ((float) mData.total / SIZE_M)));
        textMsg.setText(mData.getStateString());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_downloading_item;
    }
}
