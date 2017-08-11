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
import com.google.gson.Gson;

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

    public int albumId;

    public String albumJson;

    public int audioId;

    public String audioJson;

    //单个音频大小
    public long total;

    //当前下载的大小
    public long current;

    public String url;

    public String savePath;

    public int stateValue;

    @Transient
    ZYAlbumDetail albumDetail;

    @Transient
    ZYAudio audio;

    //专辑已经下载的大小
    @Transient
    public int albumDownloadedSize;

    //已经下载了多少集
    @Transient
    public int audioDowloadedCount;

    @Transient
    public static Object object = new Object();

    @Generated(hash = 542894390)
    public ZYDownloadEntity(String id, int albumId, String albumJson, int audioId, String audioJson, long total, long current, String url, String savePath, int stateValue) {
        this.id = id;
        this.albumId = albumId;
        this.albumJson = albumJson;
        this.audioId = audioId;
        this.audioJson = audioJson;
        this.total = total;
        this.current = current;
        this.url = url;
        this.savePath = savePath;
        this.stateValue = stateValue;
    }

    @Generated(hash = 944722397)
    public ZYDownloadEntity() {
    }

    /**
     * 根据专辑详情和音频详情创建下载对象
     *
     * @param albumDetail
     * @param audio
     * @return
     */
    public static ZYDownloadEntity createEntityByAudio(ZYAlbumDetail albumDetail, ZYAudio audio) {
        ZYDownloadEntity downloadEntity = new ZYDownloadEntity();
        downloadEntity.id = audio.id + "_" + audio.albumId;
        downloadEntity.albumId = audio.albumId;
        downloadEntity.audioId = audio.id;
        downloadEntity.total = audio.fileLength;
        downloadEntity.url = audio.fileUrl;
        downloadEntity.albumJson = new Gson().toJson(albumDetail);
        downloadEntity.audioJson = new Gson().toJson(audio);
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

    public ZYAlbumDetail getAlbumDetail() {
        if (albumDetail == null) {
            albumDetail = new Gson().fromJson(albumJson, ZYAlbumDetail.class);
        }
        return albumDetail;
    }

    public ZYAudio getAudio() {
        if (audio == null) {
            audio = new Gson().fromJson(audioJson, ZYAudio.class);
        }
        return audio;
    }

    /**
     * 如果不存在则保存 存在则修改
     *
     * @return
     */
    @Override
    public long save() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.insertOrReplace(this);
        }
    }

    /**
     * 修改
     *
     * @param needInsert 是否需要创建
     * @return
     */
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

    /**
     * 删除
     */
    @Override
    public void delete() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownloadEntityDao();
            downloadEntityDao.delete(this);
        }
    }

    /**
     * 获取已经下载的专辑对象
     *
     * @return
     */
    public static List<ZYDownloadEntity> queryDownloadedAlbums() {
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

    /**
     * 查询专辑下的音频列表
     *
     * @param albumId
     * @return
     */
    public static List<ZYDownloadEntity> queryAlbumAudios(int albumId) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.AlbumId.eq(albumId)).build().list();
        }
    }

    /**
     * 查询专辑下已经下载的音频列表
     *
     * @param albumId
     * @return
     */
    public static List<ZYDownloadEntity> queryAlbumDownloadedAudios(int albumId) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.AlbumId.eq(albumId)).build().list();
        }
    }

    /**
     * 删除专辑(同时专辑下的所有音频)
     *
     * @param albumId
     */
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

    /**
     * 查询所有已经下载的音频列表
     *
     * @return
     */
    public static List<ZYDownloadEntity> queryDownloadedAudios() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().where(ZYDownloadEntityDao.Properties.Current.eq(ZYDownloadEntityDao.Properties.Total)).build().list();
        }
    }

    /**
     * 查询音频对象(音频对象是否在下载队列中)
     *
     * @param audioId
     * @param albumId
     * @return
     */
    public static ZYDownloadEntity queryById(int audioId, int albumId) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            ZYDownloadEntity entity = downloadEntityDao.load(audioId + "_" + albumId);
            return entity;
        }
    }

    /**
     * 音频是否在下载队列中
     *
     * @param audio
     * @return
     */
    public static boolean audioIsDown(ZYAudio audio) {
        return queryById(audio.id, audio.albumId) == null ? false : true;
    }

    /**
     * 获取下载的id
     *
     * @param audioId
     * @param albumId
     * @return
     */
    public static String getEntityId(int audioId, int albumId) {
        return audioId + "_" + albumId;
    }

    /**
     * 获取正在下载的音频列表
     *
     * @return
     */
    public static List<ZYDownloadEntity> queryDownloadingAudios() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().whereOr(
                    ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.WAIT.getState()),
                    ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.DOWNING.getState()),
                    ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.START.getState())
            ).build().list();
        }
    }

    /**
     * 获取正在下载队列中的音频列表(下载中的，出错的，暂停的F)
     *
     * @return
     */
    public static List<ZYDownloadEntity> queryNotFinishedAudios() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().where(
                    ZYDownloadEntityDao.Properties.StateValue.notEq(ZYDownState.FINISH.getState())
            ).build().list();
        }
    }

    /**
     * 查询正在下载的第一个音频 没有 则查询暂停的音频 没有 则查询出错的
     *
     * @return
     */
    public static ZYDownloadEntity queryNotFinishedFristAudio() {
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

    /**
     * 删除所有没有完成的音频
     *
     * @return
     */
    public static boolean deleteNotFinishedAudios() {
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

    /**
     * 查询所有暂停的音频列表(包括出错的)
     *
     * @return
     */
    public static List<ZYDownloadEntity> queryPauseAudios() {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            return downloadEntityDao.queryBuilder().whereOr(ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.PAUSE.getState())
                    , ZYDownloadEntityDao.Properties.StateValue.eq(ZYDownState.ERROR.getState())
            ).build().list();
        }
    }

    /**
     * 修改音频列表
     *
     * @param audios
     */
    public static void updateAudios(List<ZYDownloadEntity> audios) {
        synchronized (object) {
            ZYDownloadEntityDao downloadEntityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownloadEntityDao();
            downloadEntityDao.updateInTx(audios);
        }
    }

    /**
     * 是否正在下载中(包括出错暂停)
     *
     * @param audioId
     * @param albumId
     * @return
     */
    public static boolean isDowloading(int audioId, int albumId) {
        synchronized (object) {
            ZYDownloadEntity entity = queryById(audioId, albumId);
            return entity != null && (entity.current != entity.total);
        }
    }

    /**
     * 是否已经下载
     *
     * @param audioId
     * @param albumId
     * @return
     */
    public static boolean isDowloaded(int audioId, int albumId) {
        synchronized (object) {
            ZYDownloadEntity entity = queryById(audioId, albumId);
            return entity != null && (entity.current == entity.total);
        }
    }

    public static String getDownloadeLocalPath(int audioId, int albumId) {
        ZYDownloadEntity entity = queryById(audioId, albumId);
        if (entity.isDowloaded()) {
            File file = new File(entity.savePath);
            if (file.exists()) {
                return entity.savePath;
            }
            entity.delete();
        }
        return null;
    }

    public boolean isDowloaded() {
        return stateValue == ZYDownState.FINISH.getState();
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

    public int getAlbumDownloadedSize() {
        return this.albumDownloadedSize;
    }

    public void setAlbumDownloadedSize(int albumDownloadedSize) {
        this.albumDownloadedSize = albumDownloadedSize;
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

    public String getAlbumJson() {
        return this.albumJson;
    }

    public void setAlbumJson(String albumJson) {
        this.albumJson = albumJson;
    }

    public String getAudioJson() {
        return this.audioJson;
    }

    public void setAudioJson(String audioJson) {
        this.audioJson = audioJson;
    }
}
