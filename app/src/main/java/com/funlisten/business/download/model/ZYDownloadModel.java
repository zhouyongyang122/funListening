package com.funlisten.business.download.model;

import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;

import java.util.List;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYDownloadModel extends ZYBaseModel {

    public List<ZYDownloadEntity> getAlbums() {
        return ZYDownloadEntity.queryDownloadedAlbums();
    }

    public List<ZYDownloadEntity> getAlbumAudios(int albumId, boolean asc) {
        return ZYDownloadEntity.queryAlbumAudios(albumId, asc);
    }

    public ZYDownloadEntity queryAudioByNotFinishedState() {
        return ZYDownloadEntity.queryNotFinishedFristAudio();
    }

    public List<ZYDownloadEntity> queryAudiosByNotFinishedState() {
        return ZYDownloadEntity.queryNotFinishedAudios();
    }
}
