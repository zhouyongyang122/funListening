package com.funlisten.business.download.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadingHeaderVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.textDown)
    TextView textDown;

    @Bind(R.id.textClearAll)
    TextView textClearAll;

    @Override
    public void updateView(Object data, int position) {

    }

    @OnClick({R.id.textDown, R.id.textClearAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textClearAll:
                break;
            case R.id.textDown:
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_downloading_header;
    }
}
