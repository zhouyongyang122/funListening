package com.funlisten.business.download.model.bean;

import com.funlisten.ZYApplication;
import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.db.ZYDBManager;
import com.funlisten.service.db.entity.ZYBaseEntity;
import com.funlisten.service.db.entity.ZYDownloadEntityDao;
import com.funlisten.service.downNet.down.ZYDownState;
import com.funlisten.service.downNet.down.ZYIDownBase;
import com.funlisten.utils.ZYLog;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZY on 17/7/12.
 */

@Entity
public class ZYDownloadEntity extends ZYBaseEntity implements ZYIDownBase {


    //音频id + 专辑id 作为主键(audioId_albumId)
    @Id
    public String id;

    //音频id
    public int audioId;

    //专辑id
    public int albumId;

    //专辑名称
    public String albumName;

    //专辑图片
    public String albumCoverUrl;

    //专辑作者
    public String albumPublisher;

    //专辑已经下载的大小
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
    public long total;

    //当前下载的大小
    public long current;

    public String url;

    public String savePath;

    public int stateValue;

    @Transient
    public boolean isEdit;

    @Transient
    public static Object object = new Object();

    @Generated(hash = 591050772)
    public ZYDownloadEntity(String id, int audioId, int albumId, String albumName, String albumCoverUrl, String albumPublisher, int albumDownloadedSize,
                            int audioUpatedCount, int audioDowloadedCount, int audioCount, String audioName, String audioCreateTime, int audioSort, long total, long current,
                            String url, String savePath, int stateValue) {
        this.id = id;
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
        this.total = total;
        this.current = current;
        this.url = url;
        this.savePath = savePath;
        this.stateValue = stateValue;
    }

    @Generated(hash = 944722397)
    public ZYDownloadEntity() {
    }

    public static ZYDownloadEntity createEntityByAudio(ZYAlbumDetail albumDetail, ZYAudio audio) {
        ZYDownloadEntity downloadEntity = new ZYDownloadEntity();
        downloadEntity.id = audio.id + "_" + audio.albumId;
        downloadEntity.audioId = audio.id;
        downloadEntity.albumId = audio.albumId;
        downloadEntity.albumName = albumDetail.name;
        downloadEntity.albumCoverUrl = albumDetail.coverUrl;
        downloadEntity.albumPublisher = albumDetail.publisher.nickname;
        downloadEntity.audioUpatedCount = albumDetail.audioCount;
        downloadEntity.audioCount = albumDetail.audioCount;
        downloadEntity.audioName = audio.title;
        downloadEntity.audioCreateTime = audio.gmtCreate;
        downloadEntity.audioSort = audio.sort;
        downloadEntity.total = audio.fileLength;
        downloadEntity.url = audio.fileUrl;
        File file = new File(ZYApplication.AUDIO_DOWNLOAD_DIR + albumDetail.id + "/" + audio.id + ".mp3");
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            ZYLog.e(ZYDownloadEntity.class.getSimpleName(), "createEntityByAudio: " + file.getAbsolutePath());
        } catch (Exception e) {
            ZYLog.e(ZYDownloadEntity.class.getSimpleName(), "createEntityByAudio-error: " + e.getMessage());
        }
        downloadEntity.savePath = file.getAbsolutePath();
        downloadEntity.save();
        return downloadEntity;
    }

    @Override
    public long save() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.insertOrReplace(this);
        }
    }

    @Override
    public long update(boolean needInsert) {
        synchronized (object) {
            try {
                ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
                if (!needInsert) {
                    downloadEntityDao.update(this);
                    return 1;
                }
                return downloadEntityDao.insertOrReplace(this);
            } catch (Exception e) {
                ZYLog.e(getClass().getSimpleName(), "update-error: " + e.getMessage());
            }
            return 0;
        }
    }

    @Override
    public void delete() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
            downloadEntityDao.delete(this);
        }
    }

    public static List<ZYDownloadEntity> queryAlbums() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            List<ZYDownloadEntity> downloadedAudios = downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.FINISH.getState())).build().list();
            if (downloadedAudios == null || downloadedAudios.size() <= 0) {
                return null;
            }
            Map<Integer, ZYDownloadEntity> albums = new HashMap<Integer, ZYDownloadEntity>();
            for (ZYDownloadEntity downloadEntity : downloadedAudios) {
                if (albums.containsKey(downloadEntity.albumId)) {
                    ZYDownloadEntity albumEntity = albums.get(downloadEntity.albumId);
                    albumEntity.audioDowloadedCount++;
                    albumEntity.albumDownloadedSize += downloadEntity.total;
                } else {
                    downloadEntity.audioDowloadedCount++;
                    downloadEntity.albumDownloadedSize += downloadEntity.total;
                    albums.put(downloadEntity.albumId, downloadEntity);
                }
            }

            List<ZYDownloadEntity> resutls = new ArrayList<ZYDownloadEntity>();
            resutls.addAll(albums.values());
            return resutls;
        }
    }

    public static List<ZYDownloadEntity> queryAlbumAudios(int albumId) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.AlbumId.eq(albumId)).build().list();
        }
    }

    public static void delByAlbumId(int albumId) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
            List<ZYDownloadEntity> results = downloadEntityDao.queryBuilder().where(
                    ZYDownloadEntityDao.Properties.AlbumId.eq(albumId)
            ).build().list();
            if (results != null && results.size() > 0) {
                downloadEntityDao.deleteInTx(results);
            }
        }
    }


    public static List<ZYDownloadEntity> queryDownloaded() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.Current.eq(ZYDownloadEntityDao.Properties.Total)).build().list();
        }
    }

    public static ZYDownloadEntity queryById(int audioId, int albumId) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            ZYDownloadEntity entity = downloadEntityDao.load(audioId + "_" + albumId);
            return entity;
        }
    }

    public static boolean audioIsDown(ZYAudio audio) {
        return queryById(audio.id, audio.albumId) == null ? false : true;
    }

    public static String getEntityId(int audioId, int albumId) {
        return audioId + "_" + albumId;
    }

    public static List<ZYDownloadEntity> queryAudiosByDowloadState() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().whereOr(
                    ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.WAIT.getState()),
                    ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.DOWNING.getState()),
                    ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.START.getState())
            ).build().list();
        }
    }

    public static List<ZYDownloadEntity> queryAudiosByNotFinishedState() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().where(
                    ZYDownloadEntityDao.Properties.StateValue.notEq(ZYDownState.FINISH.getState())
            ).build().list();
        }
    }

    /**
     * 查询正在下载的音频 没有 则查询暂停的音频 没有 则查询出错的
     *
     * @return
     */
    public static ZYDownloadEntity queryAudioByNotFinishedState() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            List<ZYDownloadEntity> results = downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.DOWNING.getState())).build().list();
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
            results = downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.START.getState())).build().list();
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
            results = downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.WAIT.getState())).build().list();
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
            results = downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.PAUSE.getState())).build().list();
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
            results = downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.ERROR.getState())).build().list();
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
            return null;
        }
    }

    public static boolean deleteAudiosNotFinishedState() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
            List<ZYDownloadEntity> results = downloadEntityDao.queryBuilder().where(
                    ZYDownloadEntityDao.Properties.StateValue.notEq(ZYDownState.FINISH.getState())
            ).build().list();
            if (results != null && results.size() > 0) {
                downloadEntityDao.deleteInTx(results);
            }
            return true;
        }
    }

    public static List<ZYDownloadEntity> queryAudioByPauseState() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().whereOr(ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.PAUSE.getState())
                    , ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.ERROR.getState())
            ).build().list();
        }
    }

    public static void updateAudios(List<ZYDownloadEntity> audios) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            downloadEntityDao.updateInTx(audios);
        }
    }


    public static ZYDownloadEntity queryById(String id) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            ZYDownloadEntity entity = downloadEntityDao.load(id);
            return entity;
        }
    }

    public static boolean isDowloading(int audioId, int albumId) {
        synchronized (object) {
            ZYDownloadEntity entity = queryById(audioId, albumId);
            return entity != null && (entity.current != entity.total);
        }
    }

    public static boolean isDowloaded(int audioId, int albumId) {
        synchronized (object) {
            ZYDownloadEntity entity = queryById(audioId, albumId);
            return entity != null && (entity.current == entity.total);
        }
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

    public ZYDownState getState() {
        switch (stateValue) {
            case 0:
                return ZYDownState.WAIT;
            case 1:
                return ZYDownState.START;
            case 2:
                return ZYDownState.DOWNING;
            case 3:
                return ZYDownState.PAUSE;
            case 4:
                return ZYDownState.ERROR;
            case 5:
                return ZYDownState.FINISH;
            default:
                return ZYDownState.WAIT;
        }
    }

    public String getStateString() {
        switch (stateValue) {
            case 0:
                return "等待下载";
            case 1:
                return "等待下载";
            case 2:
                return "下载中";
            case 3:
                return "已暂停";
            case 4:
                return "下载出错";
            case 5:
                return "下载完成";
            default:
                return "准备下载";
        }
    }

    public boolean isFinished() {
        return stateValue == ZYDownState.FINISH.getState();
    }

    public void setState(ZYDownState state) {
        this.stateValue = state.getState();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurrent() {
        return this.current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public int getStateValue() {
        return this.stateValue;
    }

    public void setStateValue(int stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public long update() {
        return 0;
    }
}
