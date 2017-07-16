package com.funlisten.business.play;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.funlisten.ZYPreferenceHelper;
import com.funlisten.base.player.FZAudioPlayer;
import com.funlisten.business.play.model.ZYPLayManager;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.utils.ZYLog;

/**
 * Created by ZY on 17/7/16.
 */

public class ZYPlayService extends Service {

    ZYAudio mAudio;

    FZAudioPlayer audioPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        ZYLog.e(ZYPlayService.class.getSimpleName(), "onServiceConnected");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ZYLog.e(ZYPlayService.class.getSimpleName(), "onStartCommand");

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public void play(ZYAudio audio) {
        mAudio = audio;
        ZYPLayManager.getInstance().saveLastPlayId(audio.albumId, audio.id);
        ZYLog.e(ZYPlayService.class.getSimpleName(), "play: " + audio.fileUrl);
        if (audioPlayer == null) {
            audioPlayer = new FZAudioPlayer(getApplicationContext(), "ZYPlayService");
        }
        audioPlayer.stop();
        audioPlayer.open(audio.fileUrl, 0);
    }

    public class PlayBinder extends Binder {
        //返回Service对象
        public ZYPlayService getService() {
            return ZYPlayService.this;
        }
    }

}
