package com.funlisten.business.album.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.downNet.down.ZYDownState;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.service.downNet.down.ZYIDownBase;
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

public class ZYAudioItemVH extends ZYBaseViewHolder<ZYAudio> {

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textTimeDay)
    TextView textTimeDay;

    @Bind(R.id.textPlayNum)
    TextView textPlayNum;

    @Bind(R.id.textTimeHours)
    TextView textTimeHours;

    @Bind(R.id.layoutDownload)
    RelativeLayout layoutDownload;

    @Bind(R.id.imgDownload)
    ImageView imgDownload;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.textAuditon)
    TextView textAuditon;

    @Bind(R.id.imgLock)
    ImageView imgLock;

    ZYAudio mData;

    ZYDownloadEntity mDownloadEntity;

    AudioItemListener listener;

    public ZYAudioItemVH(AudioItemListener listener) {
        this.listener = listener;
        EventBus.getDefault().register(this);
        this.listener.addEvents(this);
    }

    @Override
    public void updateView(ZYAudio data, int position) {
        if (data != null) {
            mData = data;
            textName.setText("第 " + mData.sort + " 期 | " + mData.title);
            textPlayNum.setText(mData.playCount + "");
            textTimeDay.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.YYMMDDHH));
            textTimeHours.setText(ZYDateUtils.getTimeString(mData.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.HHMM24));
            mDownloadEntity = ZYDownloadEntity.queryById(mData.id, mData.albumId);
            refreshView();
        }
    }

    private void refreshView() {
        if (listener.canDownload()) {
            layoutDownload.setVisibility(View.VISIBLE);
            textAuditon.setVisibility(View.GONE);
            imgLock.setVisibility(View.GONE);
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
        } else {
            layoutDownload.setVisibility(View.GONE);
            if (mData.isAudition()) {
                textAuditon.setVisibility(View.VISIBLE);
                imgLock.setVisibility(View.GONE);
            } else {
                textAuditon.setVisibility(View.GONE);
                imgLock.setVisibility(View.VISIBLE);
            }
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

        boolean canDownload();

        void addEvents(ZYBaseViewHolder viewHolder);
    }
}
