package com.funlisten.service.downNet.down;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ZY on 17/7/14.
 */

public class ZYDownloadThreadPool {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;

    private final int KEEP_ALIVE = 500;

    private final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "TDThreadPool #" + mCount.getAndIncrement());
        }
    };
    private BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(512);
    public ThreadPoolExecutor THREAD_POOL_EXECUTOR = null;

    public ZYDownloadThreadPool(int core_pool_size) {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(core_pool_size, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.MILLISECONDS,
                sPoolWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public void execute(Runnable command) {
        if (command == null) {
            return;
        }
        THREAD_POOL_EXECUTOR.execute(command);
    }

    // 打断线程池
    public void shutdown() {
        if (THREAD_POOL_EXECUTOR != null) {
            THREAD_POOL_EXECUTOR.shutdownNow();
        }
    }

    public void removeTask(Runnable task) {
        try {
            if (THREAD_POOL_EXECUTOR != null) {
                THREAD_POOL_EXECUTOR.remove(task);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
