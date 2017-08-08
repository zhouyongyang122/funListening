package com.funlisten.service.downNet.down;

import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.utils.ZYFileUtils;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYUrlUtils;
import com.iflytek.cloud.thirdparty.S;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ZY on 17/3/17.
 */

public class ZYDownloadManager {

    ZYDownloadThreadPool threadPool;

    private HashMap<String, DownloadTask> audios = new HashMap<String, DownloadTask>();

    private static ZYDownloadManager instance;

    private ZYDownloadManager() {
        getThreadPool();
    }

    private ZYDownloadThreadPool getThreadPool() {
        if (threadPool == null || threadPool.THREAD_POOL_EXECUTOR == null || threadPool.THREAD_POOL_EXECUTOR.isShutdown()) {
            threadPool = new ZYDownloadThreadPool(1);
        }
        return threadPool;
    }

    public static ZYDownloadManager getInstance() {
        if (instance == null) {
            synchronized (ZYDownloadManager.class) {
                if (instance == null) {
                    instance = new ZYDownloadManager();
                }
            }
        }
        return instance;
    }

    public boolean addAudio(ZYIDownBase downBase) {
        synchronized (this) {
            if (downBase == null) {
                ZYLog.e(getClass().getSimpleName(), "addDownloadAudio: downBase is null");
                return false;
            }
            if (audios.containsKey(downBase.getId())) {
                ZYLog.e(getClass().getSimpleName(), "addDownloadAudio: downBase is downloading");
                return false;
            }
            downBase.setState(ZYDownState.WAIT);
            if (downBase.save() <= 0) {
                ZYLog.e(getClass().getSimpleName(), "addDownloadAudio: downBase save fail");
                return false;
            }
            DownloadTask task = new DownloadTask(downBase);
            audios.put(downBase.getId(), task);
            getThreadPool().execute(task);
            return true;
        }
    }

    public void addAudios(ArrayList<ZYIDownBase> downBases) {
        if (downBases == null || downBases.size() <= 0) {
            return;
        }
        for (ZYIDownBase downBase : downBases) {
            addAudio(downBase);
        }
    }

    public void startAllAudio() {
        new Thread() {
            @Override
            public void run() {
                List<ZYDownloadEntity> list = ZYDownloadEntity.queryAudioByPauseState();
                if (list != null && list.size() > 0) {
                    for (ZYDownloadEntity downloadEntity : list) {
                        downloadEntity.setState(ZYDownState.WAIT);
                    }
                    ZYDownloadEntity.updateAudios(list);
                    ArrayList<ZYIDownBase> audios = new ArrayList<ZYIDownBase>();
                    audios.addAll(list);
                    addAudios(audios);
                }
            }
        }.start();
    }

    public void puaseAllAudio(boolean needCancle) {
        if (needCancle) {
            cancleAll();
        }
        new Thread() {
            @Override
            public void run() {
                List<ZYDownloadEntity> list = ZYDownloadEntity.queryAudiosByDowloadState();
                if (list != null && list.size() > 0) {
                    for (ZYDownloadEntity downloadEntity : list) {
                        downloadEntity.setState(ZYDownState.PAUSE);
                    }
                    ZYDownloadEntity.updateAudios(list);
                }
            }
        }.start();
    }

    public void deleteAllAudio() {
        cancleAll();
        ZYDownloadEntity.deleteAudiosNotFinishedState();
    }

    public void delAudio(ZYIDownBase downBase) {
        cancleAudio(downBase.getId());
        downBase.delete();
    }


    public boolean cancleAudio(String id) {
        synchronized (this) {
            if (audios.containsKey(id)) {
                DownloadTask task = audios.get(id);
                task.entity.setState(ZYDownState.PAUSE);
                getThreadPool().removeTask(task);
                task.unsubscribe();
                audios.remove(id);
                return true;
            }
            return false;
        }
    }

    public boolean cancleAll() {
        synchronized (this) {
            Collection<DownloadTask> tasks = audios.values();
            for (DownloadTask task : tasks) {
                task.entity.setState(ZYDownState.PAUSE);
                task.unsubscribe();
            }
            audios.clear();
            getThreadPool().shutdown();
            return true;
        }
    }

    public void removeTask(String id) {
        synchronized (this) {
            audios.remove(id);
        }
    }

    public class DownloadTask implements Runnable {

        ZYIDownBase entity;

        public ZYDownloadSubscriber downloadSubscriber;

        public DownloadTask(ZYIDownBase entity) {
            this.entity = entity;
        }

        @Override
        public void run() {
            //开始下载
            downloadSubscriber = new ZYDownloadSubscriber(entity);
            ZYDownloadInterceptor interceptor = new ZYDownloadInterceptor(downloadSubscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(3000, TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(ZYUrlUtils.getBasUrl(entity.getUrl()))
                    .build();
            ZYDownloadService downloadService = retrofit.create(ZYDownloadService.class);
            downloadService.download("bytes=" + entity.getCurrent() + "-", entity.getUrl())
                    .map(new Func1<ResponseBody, ZYIDownBase>() {
                        @Override
                        public ZYIDownBase call(ResponseBody responseBody) {
                            try {
                                ZYFileUtils.writeResponseBodyCache(responseBody, new File(entity.getSavePath()), entity.getCurrent(), entity.getTotal());
                            } catch (Exception e) {
                            /*失败抛出异常*/
                                throw new RuntimeException(e.getMessage());
                            }
                            return entity;
                        }
                    })
                    .subscribe(downloadSubscriber);
        }

        public void unsubscribe() {
            try {
                downloadSubscriber.unsubscribe();
            } catch (Exception e) {

            }
        }
    }
}
