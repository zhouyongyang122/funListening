package com.funlisten.business.mylike.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.event.ZYEventDowloadUpdate;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.dailylisten.view.viewholder.ZYDailyListenVH;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.favorite.ZYFavorite;
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
 * Created by gd on 2017/7/25.
 */

public class ZYMyLikeItem extends ZYBaseViewHolder<ZYFavorite> {
    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textTime)
    TextView textTime;

    @Bind(R.id.play_count)
    TextView playCount;

    @Bind(R.id.textTimeHours)
    TextView timeHours;

    @Bind(R.id.imgDownload)
    ImageView imgDownload;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    ZYDownloadEntity mDownloadEntity;

    AudioItemListener listener;

    ZYFavorite mData;

    public ZYMyLikeItem(AudioItemListener listener) {
        this.listener = listener;
        EventBus.getDefault().register(this);
        this.listener.addEvents(this);
    }

    @Override
    public void updateView(ZYFavorite data, int position) {
        if(data != null){
            mData = data;
            textName.setText(data.audio.sort+" | "+data.audio.title);
            playCount.setText(data.audio.playCount+"");
            textTime.setText(ZYDateUtils.getTimeString(mData.audio.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.YYMMDDHH));
            timeHours.setText(ZYDateUtils.getTimeString(mData.audio.gmtCreate, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.HHMM24));
            mDownloadEntity = ZYDownloadEntity.queryById(mData.audio.id, mData.audio.albumId);
            refreshView();
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_my_like_item;
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
                    mDownloadEntity = listener.onDownloadClick(mData.audio);
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
                if (dowloadUpdate.downloadEntity.getId().equals(ZYDownloadEntity.getEntityId(mData.audio.id, mData.audio.albumId))) {
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
