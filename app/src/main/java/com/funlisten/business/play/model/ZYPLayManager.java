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
import com.funlisten.utils.ZYLog;

import java.util.List;

/**
 * Created by ZY on 17/7/16.
 */

public class ZYPLayManager {

    private static ZYPLayManager instance;

    public static final String DEF_PRE_NAME = "play_manager_pre_name";

    public static final String LAST_PLAY_AUDIO_ID = "last_play_audio_id";

    public static final String LAST_PLAY_ALBUM_ID = "last_play_album_id";

    public static final String LAST_PLAY_IMG = "last_play_img";

    private SharedPreferences defPre;

    public ZYPlayService playService;

    ZYPlay mPlay;

    List<Object> mComments;

    private ZYPLayManager() {

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
            playService = ((ZYPlayService.PlayBinder) binder).getService();

        }
    };

    public static ZYPLayManager getInstance() {
        if (instance == null) {
            instance = new ZYPLayManager();
        }
        return instance;
    }

    public ZYPlay getPlay() {
        return mPlay;
    }

    public void setPlay(ZYPlay mPlay) {
        this.mPlay = mPlay;
    }

    public List<Object> getComments() {
        return mComments;
    }

    public void setComments(List<Object> mComments) {
        this.mComments = mComments;
    }

    public void saveLastPlayId(int albumId, int audioId) {
        SharedPreferences.Editor editor = getDefPre().edit();
        editor.putInt(LAST_PLAY_ALBUM_ID, albumId)
                .putInt(LAST_PLAY_AUDIO_ID, audioId).commit();
    }

    public int getLastPlayAudioId() {
        return getDefPre().getInt(LAST_PLAY_AUDIO_ID, 0);
    }

    public int getLastPlayAlbumId() {
        return getDefPre().getInt(LAST_PLAY_ALBUM_ID, 0);
    }

    public void saveLastPlayImg(String img) {
        getDefPre().edit().putString(LAST_PLAY_IMG, img).commit();
    }

    public String getLastPlayImg() {
        return getDefPre().getString(LAST_PLAY_IMG, null);
    }

    public void play(ZYAudio audio) {
        playService.play(audio);
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

    public void seekTo(float proportion) {
        playService.seekTo(proportion);
    }

    public void nextAudio() {
        playService.nextAudio();
    }

    public void preAudio() {
        playService.preAudio();
    }

    public SharedPreferences getDefPre() {
        if (defPre == null) {
            defPre = ZYApplication.getInstance().getSharedPreferences(DEF_PRE_NAME, 0);
        }
        return defPre;
    }
}
