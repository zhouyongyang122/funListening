package com.funlisten.business.dailylisten.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.dailylisten.model.bean.ZYTimeInfo;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ZYDailyTimeVH extends ZYBaseViewHolder<Object> {
    @Bind(R.id.time)
    TextView time;

    @Override
    public void updateView(Object data, int position) {
        if(data != null && data instanceof ZYTimeInfo ){
            ZYTimeInfo info = (ZYTimeInfo) data;
            time.setText(info.time);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_daily_time;
    }
}
