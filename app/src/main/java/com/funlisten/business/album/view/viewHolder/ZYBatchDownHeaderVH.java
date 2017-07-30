package com.funlisten.business.album.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYBatchDownHeaderInfo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/27.
 */

public class ZYBatchDownHeaderVH extends ZYBaseViewHolder<ZYBatchDownHeaderInfo> {

    @Bind(R.id.total_count)
    TextView totalCount;

    ZYBatchDownHeaderInfo mData;

    AudioSelectionsListener selectionsListener;

    public ZYBatchDownHeaderVH(AudioSelectionsListener selectionsListener) {
        this.selectionsListener = selectionsListener;
    }

    @Override
    public void updateView(ZYBatchDownHeaderInfo data, int position) {

        if(data != null){
            mData = data;
        }

        if(mData != null && totalCount != null){
            totalCount.setText("共"+mData.totalCount+"集");
        }

    }

    @OnClick({R.id.textChoice})
    public void OnClick(View view){
        selectionsListener.onSelections();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_batch_down_hd_item;
    }

    public interface AudioSelectionsListener{
        void onSelections();
    }
}
