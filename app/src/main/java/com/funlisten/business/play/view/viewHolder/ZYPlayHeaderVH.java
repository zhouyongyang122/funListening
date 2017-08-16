package com.funlisten.business.play.view.viewHolder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.play.ZYPlayService;
import com.funlisten.business.play.model.FZAudionPlayEvent;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import java.util.Formatter;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

import static com.funlisten.business.play.model.ZYPlayManager.STATE_BUFFERING_END;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_BUFFERING_START;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_COMPLETED;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_ERROR;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_NEED_BUY_PAUSED;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PAUSED;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PLAYING;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PREPARED;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PREPARING;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PREPARING_NEXT;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayHeaderVH extends ZYBaseViewHolder<ZYPlay> implements SeekBar.OnSeekBarChangeListener {

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.seekBar)
    SeekBar seekBar;

    @Bind(R.id.textStartTime)
    TextView textStartTime;

    @Bind(R.id.textEndTime)
    TextView textEndTime;

    @Bind(R.id.textPlayType)
    TextView textPlayType;

    @Bind(R.id.imgPre)
    ImageView imgPre;

    @Bind(R.id.imgPlay)
    ImageView imgPlay;

    @Bind(R.id.imgNext)
    ImageView imgNext;

    @Bind(R.id.textPlayList)
    TextView textPlayList;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textInfo)
    TextView textInfo;

    @Bind(R.id.textSubscribe)
    TextView textSubscribe;

    @Bind(R.id.textComment)
    TextView textComment;


    ZYPlay mData;

    PlayHeaderListener headerListener;

    // 时间格式器 用来格式化视频播放的时间
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    boolean isUpdated;

    public ZYPlayHeaderVH(PlayHeaderListener headerListener) {
        this.headerListener = headerListener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        seekBar.setOnSeekBarChangeListener(this);
        mFormatBuilder = new StringBuilder();
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    @Override
    public void updateView(ZYPlay data, int position) {
        if (data != null) {
            mData = data;
        }
        if (imgBg != null && mData != null) {
            if (isUpdated) {
                return;
            }
            isUpdated = true;
            mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
            ZYAlbumDetail albumDetail = mData.albumDetail;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, albumDetail.publisher.avatarUrl);
            textTitle.setText(albumDetail.name);
            textInfo.setText(albumDetail.favoriteCount + "人订阅 | " + albumDetail.playCount + "播放");
            textSubscribe.setText(albumDetail.isFavorite ? "已订阅" : "订阅");
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, albumDetail.coverUrl);
            refreshProgress(0, 1000);

            refreshPlayType();
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_play_header;
    }

    public void refreshPlayType() {
        Drawable drawable = null;
        if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_LOOP_TYPE) {
            drawable = mContext.getResources().getDrawable(R.drawable.btn_change_or_cycle_n);
            textPlayType.setText("循环");
        } else if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_SINTANCE_TYPE) {
            textPlayType.setText("单曲");
            drawable = mContext.getResources().getDrawable(R.drawable.icon_single_cycle);
        } else {
            textPlayType.setText("随机");
            drawable = mContext.getResources().getDrawable(R.drawable.icon_shuffle_playback);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textPlayType.setCompoundDrawables(null, drawable, null, null);
    }

    @OnClick({R.id.imgPre, R.id.imgPlay, R.id.imgNext, R.id.textPlayList, R.id.textPlayType, R.id.textSubscribe})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgPre:
                headerListener.onPreClick(mData);
                break;
            case R.id.imgPlay:
                headerListener.onPlayOrPauseClick(mData);
                break;
            case R.id.imgNext:
                headerListener.onNextClick(mData);
                break;
            case R.id.textPlayList:
                headerListener.onPlayListClick();
                break;
            case R.id.textPlayType:
                headerListener.onPlayTypeClick();
                if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_LOOP_TYPE) {
                    ZYPlayManager.getInstance().setPlayType(ZYPlayService.PLAY_RANDOM_TYPE);
                } else if (ZYPlayManager.getInstance().getPlayType() == ZYPlayService.PLAY_RANDOM_TYPE) {
                    ZYPlayManager.getInstance().setPlayType(ZYPlayService.PLAY_SINTANCE_TYPE);
                } else {
                    ZYPlayManager.getInstance().setPlayType(ZYPlayService.PLAY_LOOP_TYPE);
                }
                refreshPlayType();
                break;
            case R.id.textSubscribe:
                headerListener.onSubscribeClick(mData.albumDetail);
                break;
        }
    }

    public void updateSubscribeState(boolean isFavorite) {
        mData.albumDetail.isFavorite = isFavorite;
        if (mData.albumDetail.isFavorite) {
            textSubscribe.setText("已订阅");
        } else {
            textSubscribe.setText("订阅");
        }
    }

    /**
     * 根据秒格式化时间
     *
     * @param timeS
     * @return
     */
    private String stringForTime(int timeS) {
        int seconds = timeS % 60;
        int minutes = (timeS / 60) % 60;
        int hours = timeS / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    void refreshProgress(int currentPosition, int totalPosition) {
        if (textStartTime != null && mData != null) {
            textStartTime.setText(stringForTime(currentPosition / 1000));
            textEndTime.setText(stringForTime(mData.audio.audioTimeLength));
            float progress = ((float) currentPosition / (float) totalPosition) * 1000;
            seekBar.setProgress((int) progress);
        }
    }

    public void refreshPlayState(FZAudionPlayEvent playEvent) {
        if (imgPlay == null || mData == null) {
            return;
        }

        if (playEvent.state == STATE_ERROR) {
            imgPlay.setImageResource(R.drawable.play);
        } else if (playEvent.state == STATE_PREPARING) {
            imgPlay.setImageResource(R.drawable.suspend);
        } else if (playEvent.state == STATE_PREPARED) {
            imgPlay.setImageResource(R.drawable.suspend);
        } else if (playEvent.state == STATE_PLAYING) {
            imgPlay.setImageResource(R.drawable.suspend);
        } else if (playEvent.state == STATE_PAUSED) {
            imgPlay.setImageResource(R.drawable.play);
        } else if (playEvent.state == STATE_NEED_BUY_PAUSED) {
            imgPlay.setImageResource(R.drawable.play);
        } else if (playEvent.state == STATE_BUFFERING_START) {

        } else if (playEvent.state == STATE_BUFFERING_END) {

        } else if (playEvent.state == STATE_PREPARING_NEXT) {

        } else if (playEvent.state == STATE_COMPLETED) {
            imgPlay.setImageResource(R.drawable.play);
        }
        if (playEvent.audio != null) {
            mData.audio = playEvent.audio;
        }
        refreshProgress(playEvent.currentDuration, playEvent.totalDuration);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        ZYPlayManager.getInstance().seekTo(progress, seekBar.getMax());
    }

    public interface PlayHeaderListener {
        void onPreClick(ZYPlay play);

        void onNextClick(ZYPlay play);

        void onPlayOrPauseClick(ZYPlay play);

        void onPlayListClick();

        void onPlayTypeClick();

        void onSubscribeClick(ZYAlbumDetail detail);

    }
}
