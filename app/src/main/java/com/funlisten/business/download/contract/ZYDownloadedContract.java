package com.funlisten.business.download.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;

/**
 * Created by ZY on 17/7/13.
 */

public interface ZYDownloadedContract {

    interface IView extends ZYListDataContract.View<IPresenter> {

    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYDownloadEntity> {

    }
}
