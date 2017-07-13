package com.funlisten.business.download.presenter;

import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.download.contract.ZYDownloadHomeContract;
import com.funlisten.business.download.model.ZYDownloadModel;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYDownloadHomePresenter extends ZYListDataPresenter<ZYDownloadHomeContract.IView, ZYDownloadModel, ZYDownloadEntity> implements ZYDownloadHomeContract.IPresenter {

    public ZYDownloadHomePresenter(ZYDownloadHomeContract.IView view, ZYDownloadModel model) {
        super(view, model);
    }

    @Override
    protected void loadData() {

    }
}
