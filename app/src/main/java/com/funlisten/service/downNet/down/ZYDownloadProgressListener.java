package com.funlisten.service.downNet.down;

/**
 * Created by ZY on 17/3/17.
 */

public interface ZYDownloadProgressListener {

    /**
     * 下载进度
     */
    void update(long current, long total, boolean isDone);
}
