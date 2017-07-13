package com.funlisten.business.download.presenter;

import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.download.contract.ZYDownloadHomeContract;
import com.funlisten.business.download.model.ZYDownloadModel;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadedPresenter extends ZYListDataPresenter<ZYDownloadHomeContract.IView, ZYDownloadModel, ZYDownloadEntity> implements ZYDownloadHomeContract.IPresenter {

    public ZYDownloadedPresenter(ZYDownloadHomeContract.IView view) {
        super(view, new ZYDownloadModel());
    }

    @Override
    protected void loadData() {

    }
}
