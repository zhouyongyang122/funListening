package com.funlisten.business.login.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.login.model.bean.ZYUser;

/**
 * Created by ZY on 17/6/30.
 */

public interface ZYLoginContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void loginSuc(ZYUser user);
    }

    interface IPresenter extends ZYIBasePresenter {
        void login(String phone, String pwd);
    }
}
