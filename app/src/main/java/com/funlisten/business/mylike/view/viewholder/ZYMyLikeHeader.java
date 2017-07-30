package com.funlisten.business.mylike.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.mylike.model.bean.ZYMyLikeHeadInfo;

import butterknife.Bind;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYMyLikeHeader extends ZYBaseViewHolder<ZYMyLikeHeadInfo> {

    @Bind(R.id.textName)
    TextView textName;

    ZYMyLikeHeadInfo mData;
    @Override
    public void updateView(ZYMyLikeHeadInfo data, int position) {
        if(data!= null){
            mData = data;
        }
        if(mData != null && textName != null) textName.setText("共喜欢过"+mData.totalCount+"音频");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_my_like_header_item;
    }
}
