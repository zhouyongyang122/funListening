package com.funlisten.business.play;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.event.ZYEventPlayState;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.player.FZAudioPlayer;
import com.funlisten.base.player.FZIPlayer;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.play.model.ZYPLayManager;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.ZYLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/7/16.
 */

public class ZYPlayService extends Service implements FZIPlayer.PlayerCallBack {

    ZYAudio mAudio;

    ArrayList<ZYAudio> mAudios = new ArrayList<ZYAudio>();

    String sortType = ZYBaseModel.SORT_ASC;

    int mPosition = 0;

    int mRows = 50;

    int mPageIndex;

    FZAudioPlayer audioPlayer;

    Handler handler = new Handler();

    UpdateProgressRunnable updateProgress;

    CompositeSubscription mSubscription;

    @Override
    public void onCreate() {
        super.onCreate();
        updateProgress = new UpdateProgressRunnable();
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
        mPageIndex = mAudio.sort / mRows;
        if (mAudio.sort % mRows > 0) {
            mPageIndex += 1;
        }
        loadAudios();
        play();
    }

    public void startOrPuase() {
        if (audioPlayer.isPlaying()) {
            puase();
            return;
        }
        audioPlayer.start(true);
    }

    public void start() {
        audioPlayer.start(true);
    }

    public void puase() {
        audioPlayer.pause();
    }

    public void seekTo(int seek) {
        audioPlayer.seekTo(seek);
    }

    public void nextAudio() {
        if (mPosition < mAudios.size() - 1) {
            mAudio = mAudios.get(++mPosition);
        }
        play();
    }

    public void preAudio() {
        if (mPosition > 0) {
            mAudio = mAudios.get(--mPosition);
        }
        play();
    }

    private void play() {
        ZYPLayManager.getInstance().saveLastPlayId(mAudio.albumId, mAudio.id);
        ZYPLayManager.getInstance().saveLastPlayImg(mAudio.coverUrl);
        ZYLog.e(ZYPlayService.class.getSimpleName(), "play: " + mAudio.fileUrl);
        if (audioPlayer == null) {
            audioPlayer = new FZAudioPlayer(getApplicationContext(), "ZYPlayService");
            audioPlayer.setPlayerCallBack(this);
        }
        audioPlayer.stop();
        stopProgressUpdate();
        audioPlayer.open(mAudio.fileUrl, 0);
    }

    public class PlayBinder extends Binder {
        //返回Service对象
        public ZYPlayService getService() {
            return ZYPlayService.this;
        }
    }

    @Override
    public boolean onCallBack(String tag, int what, String msg, FZIPlayer player) {
        ZYLog.e(getClass().getSimpleName(), "ZYPlayService-onCallBack: " + player.getClass().getSimpleName() + ":" + msg);
        switch (what) {
            case FZIPlayer.PLAYER_PREPARED:
                startProgressUpdate(0);
                break;
            case FZIPlayer.PLAYER_BUFFERING_END:
                startProgressUpdate(0);
                break;
            case FZIPlayer.PLAYER_BUFFERING_START:
                stopProgressUpdate();
                break;
            case FZIPlayer.PLAYER_COMPLETIIONED:
                nextAudio();
                break;
            case FZIPlayer.PLAYER_ERROR_SYSTEM:
            case FZIPlayer.PLAYER_ERROR_UNKNOWN:
            case FZIPlayer.PLAYER_ERROR_NET:
                stopProgressUpdate();
                break;
        }
        EventBus.getDefault().post(new ZYEventPlayState(mAudio, audioPlayer.getState(), audioPlayer.getDuration(), audioPlayer.getCurrentPosition()));
        return true;
    }

    /**
     * 开始进度改变
     *
     * @param delayedTime 启动延迟时间
     */
    private void startProgressUpdate(int delayedTime) {
        if (updateProgress == null) {
            return;
        }

        stopProgressUpdate();
        if (delayedTime > 0) {
            handler.postDelayed(updateProgress, delayedTime);
        } else {
            handler.post(updateProgress);
        }
    }

    /**
     * 停止进度改变
     */
    private void stopProgressUpdate() {
        if (updateProgress != null) {
            handler.removeCallbacks(updateProgress);
        }
    }

    private class UpdateProgressRunnable implements Runnable {
        @Override
        public void run() {
            if (audioPlayer != null) {
                int currentPosition = audioPlayer.getCurrentPosition();
                handler.postDelayed(updateProgress, 500);
                EventBus.getDefault().post(new ZYEventPlayState(mAudio, audioPlayer.getState(), audioPlayer.getDuration(), currentPosition));
            }
        }
    }

    private void loadAudios() {
        getSubscription().add(ZYNetSubscription.subscription(new ZYAlbumModel().getAudios(mPageIndex, mRows, mAudio.albumId, sortType), new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYAudio>>>() {
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYAudio>> response) {
                if (response.data != null && response.data.data != null && response.data.data.size() > 0) {
                    mAudios.clear();
                    mAudios.addAll(response.data.data);
                    for (int i = 0; i < mAudios.size(); i++) {
                        ZYAudio value = mAudios.get(i);
                        if (mAudio.id == value.id) {
                            mPosition = i;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFail(String message) {

            }
        }));
    }

    private CompositeSubscription getSubscription() {
        if (mSubscription == null) {
            mSubscription = new CompositeSubscription();
        }
        return mSubscription;
    }

    private void unsubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }
}
