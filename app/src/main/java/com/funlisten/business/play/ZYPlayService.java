package com.funlisten.business.play;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.player.FZAudioPlayer;
import com.funlisten.base.player.FZIPlayer;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.play.model.FZAudionPlayEvent;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.play.model.bean.ZYPlayHistory;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.thirdParty.image.ZYIImageLoader;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYToast;
import com.funlisten.utils.ZYUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by ZY on 17/7/16.
 */

public class ZYPlayService extends Service implements FZIPlayer.PlayerCallBack {

    //顺序播放
    public static final int PLAY_LOOP_TYPE = 1;

    //单曲
    public static final int PLAY_SINTANCE_TYPE = 2;

    //随机
    public static final int PLAY_RANDOM_TYPE = 3;

    int playType = PLAY_LOOP_TYPE;

    //正在播放音频
    ZYAudio mCurrentPlayAudio;

    //缓存的音频数据列表
    List<ZYAudio> mAudios = new ArrayList<ZYAudio>();

    //音频播放器
    FZAudioPlayer audioPlayer;

    Handler handler = new Handler();

    UpdateProgressRunnable updateProgress;

    CompositeSubscription mSubscription;

    NotificationManager mNotificationManager;

    RemoteViews mRemoteView;

    Notification mNotify;

    int mNotifyId = 200;

    ZYAlbumDetail mAlbumDetail;

    ButtonBroadcastReceiver mButtonBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        updateProgress = new UpdateProgressRunnable();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ZYLog.e(getClass().getSimpleName(), "onCreate");
        mButtonBroadcastReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ButtonBroadcastReceiver.ACTION_BUTTON);
        registerReceiver(mButtonBroadcastReceiver, intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AudioBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ZYLog.e(getClass().getSimpleName(), "onStartCommand");
        return START_STICKY;
    }

    public FZAudioPlayer getAudioPlayer() {
        if (audioPlayer == null) {
            audioPlayer = new FZAudioPlayer(getApplicationContext(), "ZYPlayService");
            audioPlayer.setPlayerCallBack(this);
        }
        return audioPlayer;
    }

    public void play(ZYAudio currenPlayAudio, List<ZYAudio> audios, ZYAlbumDetail albumDetail) {
        setAudios(audios);
        if (mRemoteView == null) {
            showNotification();
        }
        if (mAlbumDetail == null || mAlbumDetail.id != albumDetail.id) {
            mAlbumDetail = albumDetail;
            downloadBg();
        }
        mAlbumDetail = albumDetail;
        mCurrentPlayAudio = currenPlayAudio;
        play();
    }

    public void setAudios(List<ZYAudio> audios) {
        mAudios.clear();
        mAudios.addAll(audios);
    }

    private void play() {
        refreshNotificationTitle();

//        if (mCurrentPlayAudio.isFree() || mCurrentPlayAudio.isAudition() || mCurrentPlayAudio.isBuy()) {
        if (!mAlbumDetail.isNeedBuy() || mAlbumDetail.isBuy || mCurrentPlayAudio.isAudition()) {
            ZYPlayHistory.saveByAudio(mCurrentPlayAudio, 0);
            getAudioPlayer().stop();
            stopProgressUpdate();
            sendCallBack(ZYPlayManager.STATE_PREPARING, "播放器初使化中");
            String localPath = ZYDownloadEntity.getDownloadeLocalPath(mCurrentPlayAudio.id, mCurrentPlayAudio.albumId);
            if (localPath == null) {
                getAudioPlayer().open(ZYApplication.getInstance().getProxy(this).getProxyUrl(mCurrentPlayAudio.fileUrl, true), 0);
            } else {
                ZYLog.e(getClass().getSimpleName(), "play:local: " + localPath);
                getAudioPlayer().open(localPath, 0);
            }
            reportAudioPlay();

            ZYNetSubscription.subscription(new ZYBaseModel().reportPlay(mCurrentPlayAudio.id), new ZYNetSubscriber<ZYResponse>() {
                @Override
                public void onFail(String message) {
                }
            });
        } else {
            getAudioPlayer().stop();
            sendCallBack(ZYPlayManager.STATE_NEED_BUY_PAUSED, "暂停播放,收费视频");
        }
    }

    public void startOrPuase() {
        if (getAudioPlayer().isPlaying()) {
            puase();
        } else {
            start();
        }
    }

    public void start() {

        if (!mAlbumDetail.isNeedBuy() || mAlbumDetail.isBuy || mCurrentPlayAudio.isAudition()) {
            if (!getAudioPlayer().isPlaying()) {
                startProgressUpdate(0);
                getAudioPlayer().start(true);
                sendCallBack(ZYPlayManager.STATE_PLAYING, "正在播放");
                refreshNotificationPlayStatus(true);
            }
        } else {
            sendCallBack(ZYPlayManager.STATE_NEED_BUY_PAUSED, "暂停播放,收费视频");
        }
    }

    public void puase() {
        if (getAudioPlayer().isPlaying()) {
            stopProgressUpdate();
            getAudioPlayer().pause();
            sendCallBack(ZYPlayManager.STATE_PAUSED, "暂停播放");
            refreshNotificationPlayStatus(false);
        }
    }

    public void seekTo(int currentProgress, int totalProgress) {
        float progress = (float) currentProgress / (float) totalProgress;
        getAudioPlayer().seekTo((int) (progress * audioPlayer.getDuration()));
    }

    @Override
    public boolean onCallBack(String tag, int what, String msg, FZIPlayer player) {
        ZYLog.e(getClass().getSimpleName(), "FZAudioPlaysevice-onCallBack: " + what + ":" + msg);

        switch (what) {
            case FZIPlayer.PLAYER_PREPARED:
                startProgressUpdate(0);
                sendCallBack(ZYPlayManager.STATE_PREPARED, "准备播放");
                refreshNotificationPlayStatus(true);
                break;
            case FZIPlayer.PLAYER_BUFFERING_END:
                startProgressUpdate(0);
                sendCallBack(ZYPlayManager.STATE_BUFFERING_END, "缓冲结束-开始播放");
                break;
            case FZIPlayer.PLAYER_BUFFERING_START:
                stopProgressUpdate();
                sendCallBack(ZYPlayManager.STATE_BUFFERING_START, "缓冲开始-等待播放");
                break;
            case FZIPlayer.PLAYER_COMPLETIIONED:
                stopProgressUpdate();
                refreshNotificationPlayStatus(false);
                playNext();
                break;
            case FZIPlayer.PLAYER_ERROR_SYSTEM:
            case FZIPlayer.PLAYER_ERROR_UNKNOWN:
            case FZIPlayer.PLAYER_ERROR_NET:
                stopProgressUpdate();
                refreshNotificationPlayStatus(false);
                sendCallBack(ZYPlayManager.STATE_ERROR, "播放出错");
                break;
        }
        return true;
    }

    private void playNext() {
        int curPosition = getCurIndex();
        int position = 0;
        if (playType == PLAY_LOOP_TYPE) {
            position = curPosition + 1;
        } else if (playType == PLAY_SINTANCE_TYPE) {
            position = curPosition;
        } else {
            position = new Random().nextInt(mAudios.size());
        }
        if (position < mAudios.size() - 1) {
            mCurrentPlayAudio = mAudios.get(position);
            sendCallBack(ZYPlayManager.STATE_PREPARING_NEXT, "准备播放下一集");
//            if (mCurrentPlayAudio.isFree() || mCurrentPlayAudio.isAudition() || mCurrentPlayAudio.isBuy()) {
            if (!mAlbumDetail.isNeedBuy() || mAlbumDetail.isBuy || mCurrentPlayAudio.isAudition()) {
                play();
            } else {
                getAudioPlayer().stop();
                sendCallBack(ZYPlayManager.STATE_NEED_BUY_PAUSED, "暂停播放,收费视频");
            }
        } else {
            getAudioPlayer().stop();
            sendCallBack(ZYPlayManager.STATE_COMPLETED, "列表播放完成");
        }
    }

    private void notificationPlayNextOrPre(boolean isNext) {
        int curPosition = getCurIndex();
        int position = 0;
        if (playType == PLAY_LOOP_TYPE) {
            position = isNext ? (curPosition + 1) : (curPosition - 1);
        } else {
            position = new Random().nextInt(mAudios.size());
        }

        boolean hasAudio = !isNext ? position >= 0 : position < (mAudios.size() - 1);
        if (hasAudio) {
            sendCallBack(ZYPlayManager.STATE_PREPARING_NEXT, isNext ? "准备播放下一集" : "准备播放上一集");
            mCurrentPlayAudio = mAudios.get(position);
//            if (mCurrentPlayAudio.isFree() || mCurrentPlayAudio.isAudition() || mCurrentPlayAudio.isBuy()) {
            if (!mAlbumDetail.isNeedBuy() || mAlbumDetail.isBuy || mCurrentPlayAudio.isAudition()) {
                play();
            } else {
                getAudioPlayer().stop();
                sendCallBack(ZYPlayManager.STATE_NEED_BUY_PAUSED, "暂停播放,收费视频");
            }
        } else {
            getAudioPlayer().stop();
        }
    }

    public boolean isPlaying() {
        if (getAudioPlayer() == null) {
            return false;
        }
        return getAudioPlayer().isPlaying();
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

    private void reportAudioPlay() {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("audio_id", mCurrentPlayAudio.getAudioId());
//        getSubscription().add(FZNetBaseSubscription.subscription(FZNetManager.shareInstance().getApi().reportAudioPlay(params), new FZNetBaseSubscriber() {
//
//        }));
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    private void sendCallBack(int state, String msg) {
        FZAudionPlayEvent playEvent = new FZAudionPlayEvent(mCurrentPlayAudio, state, msg, audioPlayer.getCurrentPosition(), audioPlayer.getDuration());
        EventBus.getDefault().post(playEvent);
    }

    private int getCurIndex() {
        int index = 0;
        try {
            for (ZYAudio audio : mAudios) {
                if (audio.id == mCurrentPlayAudio.id) {
                    return index;
                }
                index++;
            }
        } catch (Exception e) {

        }
        return 0;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mButtonBroadcastReceiver);
    }

    class UpdateProgressRunnable implements Runnable {
        @Override
        public void run() {
            if (audioPlayer != null) {
                sendCallBack(ZYPlayManager.STATE_PLAYING, "正在播放");
                handler.postDelayed(updateProgress, 500);
            }
        }
    }

    public class AudioBinder extends Binder {
        //返回Service对象
        public ZYPlayService getService() {
            return ZYPlayService.this;
        }
    }

    void showNotification() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT < 24) {
            mRemoteView = new RemoteViews(getPackageName(), R.layout.zy_view_notification_small);
        } else {
            mRemoteView = new RemoteViews(getPackageName(), R.layout.zy_view_notification);
        }
        Intent buttonIntent = new Intent(ButtonBroadcastReceiver.ACTION_BUTTON);
        buttonIntent.putExtra(ButtonBroadcastReceiver.INTENT_BUTTONID_TAG, ButtonBroadcastReceiver.BUTTON_PREV_ID);
        mRemoteView.setOnClickPendingIntent(R.id.layoutPre, PendingIntent.getBroadcast(getApplicationContext(), 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        buttonIntent.putExtra(ButtonBroadcastReceiver.INTENT_BUTTONID_TAG, ButtonBroadcastReceiver.BUTTON_PALY_ID);
        mRemoteView.setOnClickPendingIntent(R.id.layoutPlay, PendingIntent.getBroadcast(getApplicationContext(), 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        buttonIntent.putExtra(ButtonBroadcastReceiver.INTENT_BUTTONID_TAG, ButtonBroadcastReceiver.BUTTON_NEXT_ID);
        mRemoteView.setOnClickPendingIntent(R.id.layoutNext, PendingIntent.getBroadcast(getApplicationContext(), 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        buttonIntent.putExtra(ButtonBroadcastReceiver.INTENT_BUTTONID_TAG, ButtonBroadcastReceiver.BUTTON_COLSE);
        mRemoteView.setOnClickPendingIntent(R.id.layoutClose, PendingIntent.getBroadcast(getApplicationContext(), 4, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        mBuilder.setContent(mRemoteView).setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, ZYPlayActivity.createIntent(ZYApplication.getInstance().getCurrentActivity()), PendingIntent.FLAG_UPDATE_CURRENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("趣听")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)
                .setSmallIcon(R.drawable.nav_btn_quick_play_green_n);
        mNotify = mBuilder.build();
        if (Build.VERSION.SDK_INT >= 24) {
            mNotify.bigContentView = mRemoteView;
        }
        mNotify.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(mNotifyId, mNotify);
    }

    void refreshNotificationTitle() {
        mRemoteView.setTextViewText(R.id.textAlbumName, mAlbumDetail.name);
        mRemoteView.setTextViewText(R.id.textAudioName, "主播" + mAlbumDetail.publisher.nickname + ":" + mCurrentPlayAudio.title);
        mNotificationManager.notify(mNotifyId, mNotify);
    }

    void refreshNotificationPlayStatus(boolean isPlay) {
        mRemoteView.setImageViewResource(R.id.imgPlay, isPlay ? R.drawable.suspend_x : R.drawable.play);
        mNotificationManager.notify(mNotifyId, mNotify);
    }

    public void clearNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(200);
        }
    }

    void downloadBg() {
        new DownloadImageTask().execute(mAlbumDetail.coverUrl);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (mRemoteView != null) {
                if (result != null) {
                    mRemoteView.setImageViewBitmap(R.id.imgBg, result);
                } else {
                    mRemoteView.setImageViewResource(R.id.imgBg, R.mipmap.ic_launcher);
                }
                mNotificationManager.notify(mNotifyId, mNotify);
            }
        }
    }

    public class ButtonBroadcastReceiver extends BroadcastReceiver {

        public final static String ACTION_BUTTON = "ZYPLAY_ACTION_BUTTON";

        public final static String INTENT_BUTTONID_TAG = "ButtonId";
        /**
         * 上一首 按钮点击 ID
         */
        public final static int BUTTON_PREV_ID = 1;
        /**
         * 播放/暂停 按钮点击 ID
         */
        public final static int BUTTON_PALY_ID = 2;
        /**
         * 下一首 按钮点击 ID
         */
        public final static int BUTTON_NEXT_ID = 3;

        /**
         * 关闭
         */
        public final static int BUTTON_COLSE = 4;

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_PREV_ID:
                        notificationPlayNextOrPre(false);
                        break;
                    case BUTTON_PALY_ID:
                        startOrPuase();
                        break;
                    case BUTTON_NEXT_ID:
                        notificationPlayNextOrPre(true);
                        break;
                    case BUTTON_COLSE:
                        mNotificationManager.cancel(200);
                        ZYApplication.getInstance().finisedAllActivities();
                        MobclickAgent.onKillProcess(getApplicationContext());
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
