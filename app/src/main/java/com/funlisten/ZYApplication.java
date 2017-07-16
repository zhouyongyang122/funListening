package com.funlisten;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.funlisten.business.play.ZYPlayService;
import com.funlisten.service.db.ZYDBManager;
import com.funlisten.thirdParty.statistics.DataStatistics;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYUncaughtExceptionHandler;
import com.zzhoujay.richtext.RichText;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ZY on 17/4/27.
 */

public class ZYApplication extends Application implements ZYUncaughtExceptionHandler.OnUncaughtExceptionHappenListener {

    public static ZYApplication mInstance;

    public static final String APP_ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "funlistening";

    public static final String IMG_CACHE_DIR = APP_ROOT_DIR + File.separator + "imgCache" + File.separator;

    public static final String AUDIO_CACHE_DIR = APP_ROOT_DIR + File.separator + "audioCache" + File.separator;

    private Activity currentActivity;

    private ArrayList<Activity> allActivities = new ArrayList<Activity>();

    public ZYPlayService playService;

    @Override
    public void onCreate() {
        super.onCreate();

        //在主进程中进行初始化
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info : activityManager.getRunningAppProcesses()) {
            if (info.pid == android.os.Process.myPid()) {
                if (getPackageName().equals(info.processName)) {
                    init();
                }
            }
        }
    }

    public static ZYApplication getInstance() {
        return mInstance;
    }

    private void init() {
        mInstance = this;
        //日志初使化
        ZYLog.init(BuildConfig.DEBUG);
        //数据统计
        DataStatistics.init(this);
        //数据库
        ZYDBManager.getInstance();
        initFileDir();

        ZYUncaughtExceptionHandler crashHandler = ZYUncaughtExceptionHandler.getInstance();
        crashHandler.init(this, APP_ROOT_DIR, BuildConfig.DEBUG);
        crashHandler.setListener(this);

        //在这里初始化
        initBugTags();

        //富文本缓存
        RichText.initCacheDir(this);

        startPlaySer();
    }

    private void initFileDir() {
        File file = new File(IMG_CACHE_DIR);
        if (!file.exists()) {
            ZYLog.e(getClass().getSimpleName(), "IMG_CACHE_DIR: " + file.mkdirs() + file.getAbsolutePath());
        }

        file = new File(AUDIO_CACHE_DIR);
        if (!file.exists()) {
            ZYLog.e(getClass().getSimpleName(), "AUDIO_CACHE_DIR: " + file.mkdirs() + file.getAbsolutePath());
        }
    }

    public void initBugTags() {
        if (BuildConfig.DEBUG) {
            BugtagsOptions options = new BugtagsOptions.Builder()
                    .trackingLocation(true)
                    .trackingCrashLog(true).build();
            Bugtags.start(ZYAppConstants.BUGTAGS_KEY, this, Bugtags.BTGInvocationEventShake, options);
        } else {
            Bugtags.start(ZYAppConstants.BUGTAGS_KEY, this, Bugtags.BTGInvocationEventNone);
        }
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void addActivity(Activity activity) {
        allActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        allActivities.remove(activity);
    }

    public void finisedAllActivities() {
        try {
            for (Activity activity : allActivities) {
                if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                    activity.finish();
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onUncaughtExceptionHappen(Thread thread, Throwable ex) {
    }

    public void startPlaySer() {
        try {
            Intent intent = new Intent();
            intent.setClass(this, ZYPlayService.class);
            startService(intent);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {

        }
    }

    public void stopPlaySer() {
        try {
            Intent intent = new Intent();
            intent.setClass(this, ZYPlayService.class);
            stopService(intent);
            unbindService(conn);
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
            ZYLog.e(ServiceConnection.class.getSimpleName(),"onServiceConnected");
            playService = ((ZYPlayService.PlayBinder) binder).getService();

        }
    };
}
