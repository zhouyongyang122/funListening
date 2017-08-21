package com.funlisten.business.set.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.set.model.bean.ZYMsg;

/**
 * Created by ZY on 17/4/9.
 */

public interface ZYMsgContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYMsg> {

    }
}
