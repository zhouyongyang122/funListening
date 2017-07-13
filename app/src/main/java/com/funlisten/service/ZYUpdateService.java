package com.funlisten.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.funlisten.ZYApplication;

import java.io.File;

public class ZYUpdateService extends Service {

    private static final String KEY_URL = "url";

    private BroadcastReceiver receiver;

    public static Intent createIntent(String url) {
        Intent intent = new Intent(ZYApplication.getInstance(), ZYUpdateService.class);
        intent.putExtra(KEY_URL, url);
        return intent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String url = intent.getStringExtra(KEY_URL);
            final String apkName = "diandu_" + System.currentTimeMillis() + ".apk";
            if (!TextUtils.isEmpty(url)) {
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + apkName)),
                                "application/vnd.android.package-archive");
                        startActivity(intent);
                        stopSelf();
                    }
                };

                registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                try {
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));
                    request.setMimeType("application/vnd.android.package-archive");
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
                    dm.enqueue(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
