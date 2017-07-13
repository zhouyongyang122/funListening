package com.funlisten.business.main.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.main.model.bean.ZYVersion;

/**
 * Created by ZY on 17/7/3.
 */

public interface ZYMeContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void refreshUserInfo();
    }

    interface IPresenter extends ZYIBasePresenter {
        void getUserInfo();
    }
}
