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

    public ZYDownloadedPresenter(ZYDownloadHomeContract.IView view, int albumId) {
        super(view, new ZYDownloadModel());
        this.albumId = albumId;
    }

    @Override
    protected void loadData() {
        try {
            List<ZYDownloadEntity> audios = mModel.getAlbumAudios(albumId);
            mDataList.addAll(audios);
            mView.showList(false);
            mView.refresh(audios.get(0));
        } catch (Exception e) {
            ZYLog.e(getClass().getSimpleName(), "error: " + e.getMessage());
        }
    }
}
