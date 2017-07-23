package com.funlisten.business.search.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.search.model.bean.ZYSearchHistory;

import butterknife.Bind;

/**
 * Created by gd on 2017/7/20.
 */

public class ZYHistoryVH extends ZYBaseViewHolder {
    @Bind(R.id.history_tv)
    TextView history_tv;


    @Override
    public void updateView(Object data, int position) {
        if(data instanceof ZYSearchHistory){
            history_tv.setText(((ZYSearchHistory) data).history);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_history_item;
    }
}
