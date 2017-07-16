package com.funlisten.base.player;

/**
 * Created by zhouyongyang on 16/11/10.
 */

public interface FZIPlayer {

    //缓冲结束
    public static final int PLAYER_BUFFERING_END = 101;

    //缓冲开始
    public static final int PLAYER_BUFFERING_START = 102;

    //网络错误
    public static final int PLAYER_ERROR_NET = 103;

    //未知错误
    public static final int PLAYER_ERROR_UNKNOWN = 104;

    //系统错误 不支持的视频格式等
    public static final int PLAYER_ERROR_SYSTEM = 105;

    //播放结束
    public static final int PLAYER_COMPLETIIONED = 106;

    //seek结束 seek为异步执行
    public static final int PLAYER_SEEK_COMPLETIIONED = 107;

    //准备结束
    public static final int PLAYER_PREPARED = 108;

    //错误状态
    public static final int STATE_ERROR = -1;
    //销毁状态
    public static final int STATE_IDLE = 0;
    //准备中
    public static final int STATE_PREPARING = 1;
    //准备完成
    public static final int STATE_PREPARED = 2;
    //正在播放
    public static final int STATE_PLAYING = 3;
    //暂停中
    public static final int STATE_PAUSED = 4;
    //播放完成
    public static final int STATE_COMPLETED = 5;

    void open(String url, int seekTo);

    void open(String url);

    void start(boolean needSeek);

    void pause();

    void stop();

    void destory();

    void seekTo(int seekTo);

    void setPlayerCallBack(PlayerCallBack callBack);

    int getDuration();

    int getCurrentPosition();

    boolean isPlaying();

    int getState();

    interface PlayerCallBack {
        boolean onCallBack(String tag, int what, String msg, FZIPlayer player);
    }
}
