package com.funlisten.business.followfans.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.user.model.ZYUserList;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface GDFollowContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYUserList> {

    }
}
