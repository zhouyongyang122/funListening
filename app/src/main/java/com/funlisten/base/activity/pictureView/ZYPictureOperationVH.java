package com.funlisten.base.activity.pictureView;

import android.view.View;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;

public class ZYPictureOperationVH extends ZYBaseViewHolder<String> {

    private TextView mTvOperation;

    @Override
    public void findView(View view) {
        super.findView(view);
        mTvOperation = (TextView) view.findViewById(R.id.tv_operation);
    }

    @Override
    public void updateView(String data, int position) {
        mTvOperation.setText(data);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_item_picture_operation;
    }
}
