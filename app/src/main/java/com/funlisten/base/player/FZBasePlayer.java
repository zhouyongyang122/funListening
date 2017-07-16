package com.funlisten.base.player;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhouyongyang on 16/11/11.
 */

public class FZBasePlayer implements FZIPlayer, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {

    protected FZIPlayer.PlayerCallBack callBack;

    protected Context mContext;

    protected int mCurrentState = FZIPlayer.STATE_IDLE;

    protected String tag = FZBasePlayer.class.getSimpleName();

    protected Timer timer;

    protected TimerTask timerTask;

    protected int currentPosition;

    protected String url;

    @Override
    public void open(String url, int seekTo) {

    }

    @Override
    public void open(String url) {
        open(url, 0);
    }

    @Override
    public void start(boolean needSeek) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {
        release();
    }

    @Override
    public void destory() {
        release();
        mContext = null;
    }

    @Override
    public void seekTo(int seekTo) {

    }

    @Override
    public void setPlayerCallBack(PlayerCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getState() {
        return mCurrentState;
    }

    public void release() {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            int position = mp.getCurrentPosition();
            int duration = mp.getDuration();
            if (duration - position <= 5000) {
                mCurrentState = STATE_COMPLETED;
                if (callBack != null) {
                    callBack.onCallBack(tag, FZIPlayer.PLAYER_COMPLETIIONED, null, this);
                }
            } else {
                //MediaPlayer 在出错后也可能走completion回调 在此做个容错处理
                mCurrentState = STATE_ERROR;
                if (callBack != null) {
                    callBack.onCallBack(tag, FZIPlayer.PLAYER_ERROR_UNKNOWN, null, this);
                }
            }
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, FZIPlayer.PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (callBack != null) {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                return callBack.onCallBack(tag, FZIPlayer.PLAYER_BUFFERING_START, null, this);
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                return callBack.onCallBack(tag, FZIPlayer.PLAYER_BUFFERING_END, null, this);
            }
        }
        return false;
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        try {
            if (getDuration() > 0) {
                mCurrentState = STATE_PREPARED;
                if (callBack != null) {
                    callBack.onCallBack(tag, FZIPlayer.PLAYER_PREPARED, null, this);
                }
                start(true);
            } else {
                cancleTimer();
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (getDuration() > 0) {
                            mCurrentState = STATE_PREPARED;
                            if (callBack != null) {
                                callBack.onCallBack(tag, FZIPlayer.PLAYER_PREPARED, null, FZBasePlayer.this);
                            }
                            start(true);
                            cancleTimer();
                        }
                    }
                };
                timer.schedule(timerTask, 200);
            }
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            callBack.onCallBack(tag, FZIPlayer.PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (callBack != null) {
            callBack.onCallBack(tag, FZIPlayer.PLAYER_SEEK_COMPLETIIONED, null, this);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mCurrentState = STATE_ERROR;
        int errorType = -1;
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_IO:
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                errorType = PLAYER_ERROR_NET;
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                errorType = PLAYER_ERROR_UNKNOWN;
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                errorType = PLAYER_ERROR_SYSTEM;
                break;
            default:
                errorType = PLAYER_ERROR_UNKNOWN;
                break;
        }
        if (callBack != null) {
            return callBack.onCallBack(tag, errorType, null, this);
        }
        return true;
    }

    protected void cancleTimer() {
        if (timer != null) {
            try {
                timer.cancel();
                timer = null;
            } catch (Exception e) {

            }
            try {
                timerTask.cancel();
                timerTask = null;
            } catch (Exception e) {

            }
        }
    }
}
