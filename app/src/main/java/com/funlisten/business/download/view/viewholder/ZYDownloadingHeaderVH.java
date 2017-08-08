package com.funlisten.business.download.view.viewholder;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    boolean isAllDownloading;

    DownloadingHeaderListener listener;

    public ZYDownloadingHeaderVH(DownloadingHeaderListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {

    }

    @OnClick({R.id.textDown, R.id.textClearAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textClearAll:
                listener.onClearAllClick();
                break;
            case R.id.textDown:
                isAllDownloading = !isAllDownloading;
                listener.onDownAllClick(isAllDownloading);
                if (isAllDownloading) {
                    textDown.setSelected(true);
                    textDown.setText("全部暂停");
                } else {
                    textDown.setText("全部下载");
                    textDown.setSelected(false);
                }
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_downloading_header;
    }

    public interface DownloadingHeaderListener {
        void onClearAllClick();

        void onDownAllClick(boolean isDowAll);
    }
}
