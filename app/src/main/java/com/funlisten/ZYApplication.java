package com.funlisten;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.service.db.ZYDBManager;
import com.funlisten.service.downNet.down.ZYDownloadManager;
import com.funlisten.thirdParty.statistics.DataStatistics;
import com.funlisten.utils.ZYFileUtils;
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

    public static final String AUDIO_DOWNLOAD_DIR = APP_ROOT_DIR + File.separator + "audioDownload" + File.separator;

    public static final String AUDIO_CACHE_DIR = APP_ROOT_DIR + File.separator + "audioCache" + File.separator;

    private Activity currentActivity;

    private ArrayList<Activity> allActivities = new ArrayList<Activity>();

    private HttpProxyCacheServer proxy;

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

        ZYPlayManager.getInstance().startPlaySer();

        ZYDownloadManager.getInstance().puaseAllAudio(false);
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

        file = new File(AUDIO_DOWNLOAD_DIR);
        if (!file.exists()) {
            ZYLog.e(getClass().getSimpleName(), "AUDIO_DOWNLOAD_DIR: " + file.mkdirs() + file.getAbsolutePath());
        }
    }

    public void initBugTags() {
        if (BuildConfig.DEBUG) {
            BugtagsOptions options = new BugtagsOptions.Builder()
                    .trackingLocation(true)
                    .trackingCrashLog(true).build();
            Bugtags.start(ZYAppConstants.BUGTAGS_KEY, this, Bugtags.BTGInvocationEventBubble, options);
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

    public static HttpProxyCacheServer getProxy(Context context) {
        ZYApplication app = null;
        if (context == null) {
            app = ZYApplication.getInstance();
        } else {
            app = (ZYApplication) context.getApplicationContext();
        }

        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        final File file = new File(AUDIO_CACHE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        //获取缓存大小
        long defalutSize = 100 * 1024 * 1024;
        long avaliableSize = ZYFileUtils.getAvailableSDMemorySize(Environment.getExternalStorageDirectory().getAbsolutePath());
        long size = avaliableSize / 10;
        size = size < defalutSize ? defalutSize : size;

        ZYLog.e(getClass().getSimpleName(), "videoCacheSize: " + ((float) size / (1024.0f * 1024.0f)) + "M");

        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(file).maxCacheSize(size)
                .fileNameGenerator(new FileNameGenerator() {
                    @Override
                    public String generate(String url) {
                        StringBuffer sb = new StringBuffer(url);
                        String fileName = sb.substring(url.lastIndexOf("/") + 1).replace(":", "_").replace(".", "");
                        return fileName.substring(0, fileName.length() - 1);
                    }
                })
                .build();
    }
}
