package com.funlisten.business.download.presenter;

import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.download.contract.ZYDownloadedContract;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.business.main.model.ZYMainModel;

/**
 * Created by ZY on 17/7/13.
 */

public class ZYDownloadingPresenter extends ZYListDataPresenter<ZYDownloadedContract.IView, ZYMainModel, ZYDownloadEntity> implements ZYDownloadedContract.IPresenter {

    public ZYDownloadingPresenter(ZYDownloadedContract.IView view) {
        super(view, new ZYMainModel());
    }

    @Override
    protected void loadData() {

    }
}
