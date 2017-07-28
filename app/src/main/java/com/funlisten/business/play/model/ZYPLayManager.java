package com.funlisten.business.play.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.funlisten.ZYApplication;
import com.funlisten.business.play.ZYPlayService;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.play.model.bean.ZYPlay;
import com.funlisten.business.play.model.bean.ZYPlayHistory;
import com.funlisten.utils.ZYLog;

import java.util.List;

/**
 * Created by ZY on 17/7/16.
 */

public class ZYPLayManager {

    //出错状态
    public static int STATE_ERROR = 0;

    //准备播放
    public static int STATE_PREPARING = 1;

    //准备完成,开始播放
    public static int STATE_PREPARED = 2;

    //正在播放
    public static int STATE_PLAYING = 3;

    //暂停中
    public static int STATE_PAUSED = 4;

    //收费视频暂停播放
    public static int STATE_NEED_BUY_PAUSED = 5;

    //缓冲开始
    public static int STATE_BUFFERING_START = 6;

    //缓冲结束
    public static int STATE_BUFFERING_END = 7;

    //准备播放下一音频
    public static int STATE_PREPARING_NEXT = 8;

    //播放完成
    public static int STATE_COMPLETED = 9;

    private static ZYPLayManager instance;

    public ZYPlayService playService;

    private ZYPLayManager() {

    }

    public static ZYPLayManager getInstance() {
        if (instance == null) {
            instance = new ZYPLayManager();
        }
        return instance;
    }

    public void play(ZYAudio currentAudio, List<ZYAudio> audios) {
        playService.play(currentAudio, audios);
    }

    public void setAudios(List<ZYAudio> audios) {
        playService.setAudios(audios);
    }

    public void startOrPuase() {
        playService.startOrPuase();
    }

    public void start() {
        playService.start();
    }

    public void puase() {
        playService.puase();
    }

    public void seekTo(int currentProgress, int totalProgress) {
        playService.seekTo(currentProgress, totalProgress);
    }

    /**
     * 获取最后播放的历史记录
     *
     * @return
     */
    public ZYPlayHistory queryLastPlay() {
        return ZYPlayHistory.queryLastPlay();
    }

    /**
     * 是否正在播放中
     *
     * @return
     */
    public boolean isPlaying() {
        return playService.isPlaying();
    }

    public void startPlaySer() {
        try {
            Intent intent = new Intent();
            intent.setClass(ZYApplication.getInstance(), ZYPlayService.class);
            ZYApplication.getInstance().startService(intent);
            ZYApplication.getInstance().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {

        }
    }

    public void stopPlaySer() {
        try {
            Intent intent = new Intent();
            intent.setClass(ZYApplication.getInstance(), ZYPlayService.class);
            ZYApplication.getInstance().stopService(intent);
            ZYApplication.getInstance().unbindService(conn);
        } catch (Exception e) {

        }
    }

    //使用ServiceConnection来监听Service状态的变化
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            playService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //这里我们实例化audioService,通过binder来实现
            ZYLog.e(ServiceConnection.class.getSimpleName(), "onServiceConnected");
            playService = ((ZYPlayService.AudioBinder) binder).getService();

        }
    };
}
