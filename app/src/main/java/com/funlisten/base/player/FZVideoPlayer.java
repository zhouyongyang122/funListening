package com.funlisten.base.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.View;
import android.widget.VideoView;

/**
 * Created by zhouyongyang on 16/11/11.
 */

public class FZVideoPlayer extends FZBasePlayer implements FZIPlayer, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {

    private VideoView videoView;

    //是否为静音模式
    private boolean isQuietMode = false;


    public FZVideoPlayer(VideoView videoView, Context context, String tag) {
        init(videoView, context, false, tag);
    }


    public FZVideoPlayer(VideoView videoView, Context context, boolean isQuietMode, String tag) {
        init(videoView, context, isQuietMode, tag);
    }

    private void init(VideoView videoView, Context context, boolean isQuietMode, String tag) {
        this.mContext = context;
        this.videoView = videoView;
        this.isQuietMode = isQuietMode;
        this.tag = tag;
        videoView.setOnPreparedListener(this);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(this);
            }
        } catch (Exception e) {

        }
        videoView.setOnErrorListener(this);
        videoView.setOnCompletionListener(this);
    }


    @Override
    public void open(String url, int seekTo) {
        if (url == null) {
            return;
        }
        currentPosition = seekTo;
        this.url = url;
        release();
        mCurrentState = STATE_PREPARING;
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoPath(url);
        videoView.setBackgroundColor(0);
        videoView.requestFocus();
    }

    @Override
    public void start(boolean needSeek) {
        try {
            if (!videoView.isPlaying()) {
                if (needSeek && currentPosition > 0) {
                    videoView.seekTo(currentPosition);
                }
                videoView.start();
                mCurrentState = STATE_PLAYING;
            }
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public void pause() {
        try {
            if (videoView != null && videoView.isPlaying()) {
                videoView.pause();
                getCurrentPosition();
                mCurrentState = STATE_PAUSED;
            }
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (isQuietMode) {
            mp.setVolume(0, 0);
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mp.setOnInfoListener(this);
            }
        } catch (Exception e) {

        }
        super.onPrepared(mp);
    }

    @Override
    public void seekTo(int seekTo) {
        if (videoView != null) {
            currentPosition = seekTo;
            videoView.seekTo(seekTo);
        }
    }

    @Override
    public int getDuration() {
        if (videoView != null) {
            return videoView.getDuration();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        if (videoView != null) {
            return videoView.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if (videoView != null) {
            currentPosition = videoView.getCurrentPosition();
        }
        return currentPosition;
    }

    @Override
    public void release() {
        cancleTimer();
        if (videoView != null) {
            try {
                mCurrentState = STATE_IDLE;
                videoView.setVisibility(View.GONE);
//                videoView.suspend();
                videoView.stopPlayback();
            } catch (Exception e) {

            }
        }
    }
}
