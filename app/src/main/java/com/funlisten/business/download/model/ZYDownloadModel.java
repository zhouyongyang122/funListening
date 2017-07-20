package com.funlisten.business.download.model;

import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;

import java.util.List;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYDownloadModel extends ZYBaseModel {

    public List<ZYDownloadEntity> getAlbums() {
        return ZYDownloadEntity.queryAlbums();
    }

    public List<ZYDownloadEntity> getAlbumAudios(int albumId) {
        return ZYDownloadEntity.queryAlbumAudios(albumId);
    }

    public ZYDownloadEntity queryAudioByNotFinishedState() {
        return ZYDownloadEntity.queryAudioByNotFinishedState();
    }

    public List<ZYDownloadEntity> queryAudiosByNotFinishedState() {
        return ZYDownloadEntity.queryAudiosByNotFinishedState();
    }
}
