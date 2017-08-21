package com.funlisten.business.followfans.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.set.model.bean.ZYUserList;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface ZYFollowContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
    }

    interface IPresenter extends ZYListDataContract.Presenter<ZYUserList> {

        void follow(final ZYUserList user);

        void followCancle(final ZYUserList user);
    }
}
