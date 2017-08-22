package com.funlisten.business.set.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.set.model.bean.ZYMsg;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/9.
 */

public class ZYMsgVH extends ZYBaseViewHolder<ZYMsg> {

    @Bind(R.id.textMsg)
    TextView textMsg;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.layoutReply)
    LinearLayout layoutReply;

    @Bind(R.id.textReply)
    TextView textReply;

    @Override
    public void updateView(ZYMsg data, int position) {
        if (data != null) {
            textMsg.setText(data.content);
            textTime.setText(data.gmtCreate);
            if (TextUtils.isEmpty(data.reply)) {
                layoutReply.setVisibility(View.GONE);
            } else {
                layoutReply.setVisibility(View.VISIBLE);
                textReply.setText(data.reply);
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_msg_item;
    }
}
