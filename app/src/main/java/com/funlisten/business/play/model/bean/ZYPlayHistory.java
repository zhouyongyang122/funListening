package com.funlisten.business.play.model.bean;

import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.service.db.ZYDBManager;
import com.funlisten.service.db.entity.ZYBaseEntity;
import com.funlisten.service.db.entity.ZYDownloadEntityDao;
import com.funlisten.service.db.entity.ZYPlayHistoryDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by ZY on 17/7/26.
 */

@Entity
public class ZYPlayHistory extends ZYBaseEntity {

    @Id
    public String albumId;

    public String audioId;

    public int position;

    public String img;

    public long currentDuration;

    public long lastUpdate;

    @Transient
    public static Object object = new Object();

    @Generated(hash = 1855505114)
    public ZYPlayHistory(String albumId, String audioId, int position, String img,
                         long currentDuration, long lastUpdate) {
        this.albumId = albumId;
        this.audioId = audioId;
        this.position = position;
        this.img = img;
        this.currentDuration = currentDuration;
        this.lastUpdate = lastUpdate;
    }

    @Generated(hash = 1135958132)
    public ZYPlayHistory() {
    }

    @Override
    public long save() {
        synchronized (object) {
            ZYPlayHistoryDao dao = ZYDBManager.getInstance().getWritableDaoSession().getZYPlayHistoryDao();
            return dao.insertOrReplace(this);
        }
    }

    @Override
    public long update() {
        synchronized (object) {
            ZYPlayHistoryDao dao = ZYDBManager.getInstance().getWritableDaoSession().getZYPlayHistoryDao();
            return dao.insertOrReplace(this);
        }
    }

    @Override
    public void delete() {
        synchronized (object) {
            ZYPlayHistoryDao dao = ZYDBManager.getInstance().getWritableDaoSession().getZYPlayHistoryDao();
            dao.delete(this);
        }
    }

    public static void saveByAudio(ZYAudio audio, long currentDuration) {
        ZYPlayHistory playHistory = new ZYPlayHistory();
        playHistory.albumId = audio.albumId + "";
        playHistory.audioId = audio.id + "";
        playHistory.position = audio.sort;
        playHistory.currentDuration = currentDuration;
        playHistory.lastUpdate = System.currentTimeMillis();
        playHistory.img = audio.coverUrl;
        playHistory.update();
    }

    public static ZYPlayHistory queryLastPlay() {
        synchronized (object) {
            ZYPlayHistoryDao dao = ZYDBManager.getInstance().getReadableDaoSession().getZYPlayHistoryDao();
            List<ZYPlayHistory> results = dao.queryBuilder().orderDesc(ZYPlayHistoryDao.Properties.LastUpdate).build().list();
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
            return null;
        }
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAudioId() {
        return this.audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCurrentDuration() {
        return this.currentDuration;
    }

    public void setCurrentDuration(long currentDuration) {
        this.currentDuration = currentDuration;
    }

    public long getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public long update(boolean needInsert) {
        return 0;
    }
}
