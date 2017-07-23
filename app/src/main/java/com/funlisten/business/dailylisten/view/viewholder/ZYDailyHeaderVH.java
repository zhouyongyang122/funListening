package com.funlisten.business.dailylisten.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.dailylisten.model.bean.ZYHeaderInfo;
import com.funlisten.business.profile.model.bean.ZYProfileHeaderInfo;

import butterknife.Bind;

/**
 * Created by gd on 2017/7/20.
 */

public class ZYDailyHeaderVH extends ZYBaseViewHolder<Object> {
    @Bind(R.id.total_count)
    TextView totalCount;
    ZYHeaderInfo info;

    @Override
    public void updateView(Object data, int position) {
        if(data != null && data instanceof  ZYHeaderInfo){
            info = (ZYHeaderInfo) data;
        }
        if(info != null && totalCount != null) totalCount.setText("已更新 "+info.totalCount+"");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_daily_header_item;
    }
}
