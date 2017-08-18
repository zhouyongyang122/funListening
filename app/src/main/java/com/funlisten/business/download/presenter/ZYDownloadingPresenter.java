package com.funlisten.business.download.presenter;

import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.download.contract.ZYDownloadedContract;
import com.funlisten.business.download.model.ZYDownloadModel;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.main.model.ZYMainModel;
import com.funlisten.service.downNet.down.ZYDownState;

import java.util.List;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadingPresenter extends ZYListDataPresenter<ZYDownloadedContract.IView, ZYDownloadModel, ZYDownloadEntity> implements ZYDownloadedContract.IPresenter {

    public ZYDownloadingPresenter(ZYDownloadedContract.IView view) {
        super(view, new ZYDownloadModel());
    }

    @Override
    protected void loadData() {
        mDataList.clear();
        List<ZYDownloadEntity> resutls = mModel.queryAudiosByNotFinishedState();
        if (resutls != null && resutls.size() > 0) {

            boolean hasPauseEntity = false;

            for (ZYDownloadEntity entity : resutls) {
                if (entity.getState() == ZYDownState.ERROR || entity.getState() == ZYDownState.PAUSE) {
                    hasPauseEntity = true;
                    break;
                }
            }
            mDataList.addAll(resutls);
            mView.showList(false);
            mView.refreshDownloadAllState(hasPauseEntity);
        } else {
            mView.showEmpty();
        }
    }
}
