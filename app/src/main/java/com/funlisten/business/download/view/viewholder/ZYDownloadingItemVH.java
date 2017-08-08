package com.funlisten.business.download.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.downNet.down.ZYDownState;
import com.funlisten.utils.ZYDateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by ZY on 17/7/20.
 */

public class ZYDownloadingItemVH extends ZYBaseViewHolder<ZYDownloadEntity> {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textSize)
    TextView textSize;

    @Bind(R.id.textMsg)
    TextView textMsg;

    ZYDownloadEntity mData;

    final float SIZE_M = 1024 * 1204.0f;

    DownloadingItemListener listener;

    public ZYDownloadingItemVH(DownloadingItemListener listener) {
        this.listener = listener;
        EventBus.getDefault().register(this);
        this.listener.addEvents(this);
    }

    @Override
    public void updateView(ZYDownloadEntity data, int position) {
        if (data != null) {
            mData = data;
            textName.setText("第 " + data.audioSort + " 期 | " + mData.audioName);
            refresh();
        }
    }

    private void refresh() {
        if (textSize != null) {
            textSize.setText(String.format("%.2fM", ((float) mData.current / SIZE_M)) + "/" + String.format("%.2fM", ((float) mData.total / SIZE_M)));
            textMsg.setText(mData.getStateString());
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_downloading_item;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventDowloadUpdate dowloadUpdate) {
        if (dowloadUpdate.downloadEntity != null) {
            try {
                if (dowloadUpdate.downloadEntity.getId().equals(mData.id)) {
                    ZYDownloadEntity result = (ZYDownloadEntity) dowloadUpdate.downloadEntity;
                    mData.current = result.current;
                    mData.stateValue = result.stateValue;
                    refresh();
                    if (mData.stateValue == ZYDownState.FINISH.getState()) {
                        listener.downloadFinished(mData);
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    public interface DownloadingItemListener {
        void addEvents(ZYBaseViewHolder viewHolder);

        void downloadFinished(ZYDownloadEntity data);
    }
}
