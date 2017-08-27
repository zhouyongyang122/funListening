package com.funlisten.base.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;

import com.funlisten.ZYApplication;

/**
 * Created by zhouyongyang on 16/11/10.
 */

public class FZAudioPlayer extends FZBasePlayer {

    private MediaPlayer mMediaPlayer;

    private boolean canStart = true;

    private static boolean isRequestAudioFoucus = false;

    public FZAudioPlayer(Context context, String tag) {
        this.mContext = context;
        this.tag = tag;
    }

    @Override
    public void open(String url, int seekTo) {
        if (url == null) {
            return;
        }
        canStart = true;
        currentPosition = seekTo;
        this.url = url;
        release();
        try {
            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            if (!isRequestAudioFoucus) {
                isRequestAudioFoucus = true;
                am.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        try {
                            switch (focusChange) {
                                case AudioManager.AUDIOFOCUS_GAIN:
                                    // 获得音频焦点
                                    if (!mMediaPlayer.isPlaying()) {
                                        mMediaPlayer.start();
                                    }
                                    // 还原音量
                                    mMediaPlayer.setVolume(1.0f, 1.0f);
                                    break;

                                case AudioManager.AUDIOFOCUS_LOSS:
                                    // 长久的失去音频焦点，释放MediaPlayer
//                                stop();
                                    break;

                                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                                    // 展示失去音频焦点，暂停播放等待重新获得音频焦点
                                    if (mMediaPlayer.isPlaying())
                                        mMediaPlayer.pause();
                                    break;
                                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                                    // 失去音频焦点，无需停止播放，降低声音即可
                                    if (mMediaPlayer.isPlaying()) {
                                        mMediaPlayer.setVolume(0.1f, 0.1f);
                                    }
                                    break;
                            }
                        } catch (Exception e) {

                        }
                    }
                }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setWakeMode(ZYApplication.getInstance().getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnInfoListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, FZIPlayer.PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public void start(boolean needSeek) {
        try {
            if (!canStart) {
                return;
            }
            if (!mMediaPlayer.isPlaying()) {
                if (needSeek && currentPosition > 0) {
                    mMediaPlayer.seekTo(currentPosition);
                }
                mMediaPlayer.start();
                mCurrentState = STATE_PLAYING;
            }
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, FZIPlayer.PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public void pause() {
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            if (callBack != null) {
                callBack.onCallBack(tag, FZIPlayer.PLAYER_ERROR_UNKNOWN, e.getMessage(), this);
            }
        }
    }

    @Override
    public void seekTo(int seekTo) {
        if (mMediaPlayer != null) {
            currentPosition = seekTo;
            mMediaPlayer.seekTo(seekTo);
        }
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            currentPosition = mMediaPlayer.getCurrentPosition();
        }
        return currentPosition;
    }

    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
    }

    public boolean isCanStart() {
        return canStart;
    }

    @Override
    public void release() {
        cancleTimer();
        if (mMediaPlayer != null) {
            try {
                mCurrentState = STATE_IDLE;
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                try {
                    mMediaPlayer.release();
                } catch (Exception ex) {

                }
                mMediaPlayer = null;
            }
        }

        if (mContext != null) {
            try {
                AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                am.abandonAudioFocus(null);
            } catch (Exception e) {

            }
        }
    }
}
