package com.funlisten.business.play.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.base.player.FZIPlayer;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import java.util.Formatter;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/10.
 */

public class ZYPlayHeaderVH extends ZYBaseViewHolder<ZYPlay> {

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

    // 时间格式器 用来格式化视频播放的时间
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    @Override
    public void updateView(ZYPlay data, int position) {

        if (data != null) {
            mData = data;
        }

        if (imgBg != null && mData != null) {
            mFormatBuilder = new StringBuilder();
            mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
            ZYAlbumDetail albumDetail = mData.albumDetail;
            ZYAudio audio = mData.audio;
            ZYImageLoadHelper.getImageLoader().loadImage(this, imgBg, albumDetail.coverUrl);
            textStartTime.setText("00:00");
            textEndTime.setText(stringForTime(audio.audioTimeLength));

            ZYImageLoadHelper.getImageLoader().loadImage(this, imgAvatar, albumDetail.publisher.avatarUrl);
            textTitle.setText(albumDetail.title);
            textInfo.setText(albumDetail.favoriteCount + "人订阅 | " + albumDetail.playCount + "播放");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_play_header;
    }

    @OnClick({R.id.imgPre, R.id.imgPlay, R.id.imgNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgPre:
                ZYApplication.getInstance().playService.preAudio();
                break;
            case R.id.imgPlay:
                ZYApplication.getInstance().playService.startOrPuase();
                break;
            case R.id.imgNext:
                ZYApplication.getInstance().playService.nextAudio();
                break;
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

    public void refreshProgress(int currentPosition, int totalPosition) {
        if (textStartTime != null) {
            textStartTime.setText(stringForTime(currentPosition));
            float progress = ((float) currentPosition / (float) totalPosition) * 1000;
            seekBar.setProgress((int) progress);
        }
    }

    public void refreshPlayState(int state) {
        if (imgPlay == null) {
            return;
        }
        if (state == FZIPlayer.STATE_COMPLETED || state == FZIPlayer.STATE_PAUSED) {
            imgPlay.setImageResource(R.drawable.btn_zantingbofang_n);
        } else if (state == FZIPlayer.STATE_PLAYING || state == FZIPlayer.STATE_PREPARED || state == FZIPlayer.STATE_PREPARING) {
            imgPlay.setImageResource(R.drawable.btn_zantingbofang_n);
        }
    }
}
