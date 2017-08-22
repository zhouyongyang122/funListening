package com.funlisten.business.search.view.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.search.model.bean.ZYAudioAndAlbumInfo;
import com.funlisten.service.downNet.down.ZYDownState;
import com.funlisten.utils.ZYDateUtils;
import com.funlisten.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYSearchAudioItemVH extends ZYBaseViewHolder<ZYAudioAndAlbumInfo> {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textTimeDay)
    TextView textTimeDay;

    @Bind(R.id.textPlayNum)
    TextView textPlayNum;

    @Bind(R.id.textTimeHours)
    TextView textTimeHours;

    @Bind(R.id.imgDownload)
    ImageView imgDownload;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.layoutDownload)
    RelativeLayout layoutDownload;

    @Bind(R.id.imgLock)
    ImageView imgLock;

    ZYAudio mData;


    ZYDownloadEntity mDownloadEntity;

    AudioItemListener listener;

    public ZYSearchAudioItemVH(AudioItemListener listener) {
        this.listener = listener;
        EventBus.getDefault().register(this);
        this.listener.addEvents(this);
    }

    @Override
    public void updateView(ZYAudioAndAlbumInfo data, int position) {
        if (data != null && data.audio != null) {
            mData = data.audio;
            textName.setText("第 " + mData.sort + " 期 | " + mData.title);
            textPlayNum.setText(mData.playCount + "");
            textTimeDay.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.YYMMDDHH));
            textTimeHours.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.HHMM24));
            mDownloadEntity = ZYDownloadEntity.queryById(mData.id, mData.albumId);
            if(TextUtils.isEmpty(data.audio.fileUrl)){
                layoutDownload.setVisibility(View.GONE);
                imgLock.setVisibility(View.VISIBLE);
            }else {
                layoutDownload.setVisibility(View.VISIBLE);
                imgLock.setVisibility(View.GONE);
            }
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

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_audio_item;
    }

    @OnClick({R.id.layoutDownload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutDownload:
                if (!imgDownload.isSelected()) {
                    ZYToast.show(mContext, "开始下载!");
                    if (TextUtils.isEmpty(mData.fileUrl)) {
                        ZYToast.show(mContext, "收费音频,需要先购买哦!");
                        return;
                    }
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

    public interface AudioItemListener {
        ZYDownloadEntity onDownloadClick(ZYAudio audio);

        void addEvents(ZYBaseViewHolder viewHolder);
    }
}
