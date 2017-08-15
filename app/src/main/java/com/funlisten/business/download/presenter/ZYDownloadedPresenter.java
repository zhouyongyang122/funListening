package com.funlisten.business.download.presenter;

import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.download.contract.ZYDownloadHomeContract;
import com.funlisten.business.download.model.ZYDownloadModel;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.utils.ZYLog;

import java.util.List;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadedPresenter extends ZYListDataPresenter<ZYDownloadHomeContract.IView, ZYDownloadModel, ZYDownloadEntity> implements ZYDownloadHomeContract.IPresenter {

    int albumId;

    boolean isAsc = true;

    public ZYDownloadedPresenter(ZYDownloadHomeContract.IView view, int albumId) {
        super(view, new ZYDownloadModel());
        this.albumId = albumId;
    }

    @Override
    protected void loadData() {
        try {
            List<ZYDownloadEntity> audios = mModel.getAlbumAudios(albumId, isAsc);
            int totalSize = 0;
            for (ZYDownloadEntity downloadEntity : audios) {
                totalSize += downloadEntity.total;
            }
            getDataList().clear();
            getDataList().addAll(audios);
            mView.showList(false);
            ZYDownloadEntity album = audios.get(0);
            album.albumDownloadedSize = totalSize;
            mView.refresh(album);
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "error: " + e.getMessage());
        }
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }
}
