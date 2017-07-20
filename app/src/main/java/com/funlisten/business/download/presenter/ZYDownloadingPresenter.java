package com.funlisten.business.download.presenter;

import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.download.contract.ZYDownloadedContract;
import com.funlisten.business.download.model.ZYDownloadModel;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.main.model.ZYMainModel;

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
        List<ZYDownloadEntity> resutls = mModel.queryAudiosByNotFinishedState();
        if (resutls != null && resutls.size() > 0) {
            mDataList.addAll(resutls);
            mView.showList(false);
        } else {
            mView.showEmpty();
        }
    }
}
