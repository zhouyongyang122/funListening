package com.funlisten.business.set.view;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.set.model.bean.ZYMsg;
import com.funlisten.utils.ZYDateUtils;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/9.
 */

public class ZYMsgVH extends ZYBaseViewHolder<ZYMsg> {

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textMsg)
    TextView textMsg;

    @Bind(R.id.textTime)
    TextView textTime;

    @Override
    public void updateView(ZYMsg data, int position) {
        if (data != null) {
            textTitle.setText(data.title);
            textMsg.setText(data.content);
            textTime.setText(ZYDateUtils.getTimeString(data.create_time * 1000, ZYDateUtils.YYMMDDHHMM12));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_msg_item;
    }
}
