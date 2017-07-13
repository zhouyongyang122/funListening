package com.funlisten.business.download.model.bean;

import com.funlisten.service.db.ZYDBManager;
import com.funlisten.service.db.entity.ZYBaseEntity;
import com.funlisten.service.db.entity.ZYDownloadEntityDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by ZY on 17/7/12.
 */

@Entity
public class ZYDownloadEntity extends ZYBaseEntity {

    //音频id
    @Id
    public int audioId;

    //专辑id
    public int albumId;

    //专辑名称
    public String albumName;

    //专辑图片
    public String albumCoverUrl;

    //专辑作者
    public String albumPublisher;

    //专辑下载大小
    public int albumDownloadedSize;

    //已经更新到多少集
    public int audioUpatedCount;

    //已经下载了多少集
    public int audioDowloadedCount;

    //专辑拥有的音频数量
    public int audioCount;

    //音频名称
    public String audioName;

    //音频创建时间
    public String audioCreateTime;

    //音频当前的序列
    public int audioSort;

    //单个音频大小
    public long size;

    //当前下载的大小
    public long currentSize;

    public String url;

    public String path;

    @Transient
    public boolean isEdit;

    @Generated(hash = 1064862213)
    public ZYDownloadEntity(int audioId, int albumId, String albumName, String albumCoverUrl, String albumPublisher, int albumDownloadedSize, int audioUpatedCount,
            int audioDowloadedCount, int audioCount, String audioName, String audioCreateTime, int audioSort, long size, long currentSize, String url,
            String path) {
        this.audioId = audioId;
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
        this.albumPublisher = albumPublisher;
        this.albumDownloadedSize = albumDownloadedSize;
        this.audioUpatedCount = audioUpatedCount;
        this.audioDowloadedCount = audioDowloadedCount;
        this.audioCount = audioCount;
        this.audioName = audioName;
        this.audioCreateTime = audioCreateTime;
        this.audioSort = audioSort;
        this.size = size;
        this.currentSize = currentSize;
        this.url = url;
        this.path = path;
    }

    @Generated(hash = 944722397)
    public ZYDownloadEntity() {
    }

    @Override
    public long save() {
        ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
        return downloadEntityDao.insertOrReplace(this);
    }

    @Override
    public long update() {
        ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
        return downloadEntityDao.insertOrReplace(this);
    }

    @Override
    public void delete() {
        ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
        downloadEntityDao.delete(this);
    }

    public static List<ZYDownloadEntity> queryDownloadings() {
        ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
        return downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.CurrentSize.notEq(ZYDownloadEntityDao.Properties.Size)).build().list();
    }

    public static ZYDownloadEntity queryById(int audioId) {
        ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
        ZYDownloadEntity entity = downloadEntityDao.load(audioId);
        return entity;
    }

    public static boolean isDowloading(int audioId) {
        ZYDownloadEntity entity = queryById(audioId);
        return entity != null && (entity.currentSize != entity.size);
    }

    public static boolean isDowloaded(int audioId) {
        ZYDownloadEntity entity = queryById(audioId);
        return entity != null && (entity.currentSize == entity.size);
    }

    public int getAudioId() {
        return this.audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public int getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCoverUrl() {
        return this.albumCoverUrl;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }

    public String getAlbumPublisher() {
        return this.albumPublisher;
    }

    public void setAlbumPublisher(String albumPublisher) {
        this.albumPublisher = albumPublisher;
    }

    public int getAudioCount() {
        return this.audioCount;
    }

    public void setAudioCount(int audioCount) {
        this.audioCount = audioCount;
    }

    public String getAudioName() {
        return this.audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getAudioCreateTime() {
        return this.audioCreateTime;
    }

    public void setAudioCreateTime(String audioCreateTime) {
        this.audioCreateTime = audioCreateTime;
    }

    public int getAudioSort() {
        return this.audioSort;
    }

    public void setAudioSort(int audioSort) {
        this.audioSort = audioSort;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrentSize() {
        return this.currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public int getAlbumDownloadedSize() {
        return this.albumDownloadedSize;
    }

    public void setAlbumDownloadedSize(int albumDownloadedSize) {
        this.albumDownloadedSize = albumDownloadedSize;
    }

    public int getAudioUpatedCount() {
        return this.audioUpatedCount;
    }

    public void setAudioUpatedCount(int audioUpatedCount) {
        this.audioUpatedCount = audioUpatedCount;
    }

    public int getAudioDowloadedCount() {
        return this.audioDowloadedCount;
    }

    public void setAudioDowloadedCount(int audioDowloadedCount) {
        this.audioDowloadedCount = audioDowloadedCount;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
