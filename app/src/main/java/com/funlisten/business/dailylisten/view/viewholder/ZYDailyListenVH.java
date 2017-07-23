package com.funlisten.business.dailylisten.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.view.viewHolder.ZYAudioItemVH;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.downNet.down.ZYDownState;
import com.funlisten.utils.ZYDateUtils;
import com.funlisten.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/20.
 */

public class ZYDailyListenVH extends ZYBaseViewHolder<Object> {
    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textPlayNum)
    TextView textPlayNum;

    @Bind(R.id.textTimeHours)
    TextView textTime;

    @Bind(R.id.imgDownload)
    ImageView imgDownload;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    ZYAudio mData;

    ZYDownloadEntity mDownloadEntity;

    AudioItemListener listener;

    public ZYDailyListenVH(AudioItemListener listener) {
        this.listener = listener;
        EventBus.getDefault().register(this);
        this.listener.addEvents(this);
    }

    @Override
    public void updateView(Object data, int position) {
        if (data != null && data instanceof ZYAudio ) {
            mData = (ZYAudio) data;
            textName.setText(mData.title);
            textPlayNum.setText(mData.playCount + "");
            textTime.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.HHMM24));
            mDownloadEntity = ZYDownloadEntity.queryById(mData.id, mData.albumId);
            refreshView();
        }
    }

    private void refreshView() {
        if (mDownloadEntity != null) {
            if (mDownloadEntity.getState() == ZYDownState.FINISH) {
                progressBar.setVisibility(View.GONE);
                imgDownload.setVisibility(View.VISIBLE);
                imgDownload.setSelected(true);
            } else if (mDownloadEntity.getState() == ZYDownState.ERROR || mDownloadEntity.getState() == ZYDownState.PAUSE) {
                progressBar.setVisibility(View.GONE);
                imgDownload.setVisibility(View.VISIBLE);
                imgDownload.setSelected(false);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                imgDownload.setVisibility(View.GONE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            imgDownload.setVisibility(View.VISIBLE);
            imgDownload.setSelected(false);
        }
    }

    @OnClick({R.id.layoutDownload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutDownload:
                if (!imgDownload.isSelected()) {
                    ZYToast.show(mContext, "开始下载!");
                    mDownloadEntity = listener.onDownloadClick(mData);
                    refreshView();
                } else {
                    ZYToast.show(mContext, "已经下载!");
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ZYEventDowloadUpdate dowloadUpdate) {
        if (dowloadUpdate.downloadEntity != null) {
            try {
                if (progressBar == null) {
                    EventBus.getDefault().unregister(this);
                }
                if (dowloadUpdate.downloadEntity.getId().equals(ZYDownloadEntity.getEntityId(mData.id, mData.albumId))) {
                    mDownloadEntity = (ZYDownloadEntity) dowloadUpdate.downloadEntity;
                    refreshView();
                }
            } catch (Exception e) {

            }
        }
    }
    @Override
    public int getLayoutResId() {
        return R.layout.gd_daily_listem_item;
    }

    public interface AudioItemListener {
        ZYDownloadEntity onDownloadClick(ZYAudio audio);

        void addEvents(ZYBaseViewHolder viewHolder);
    }
}
