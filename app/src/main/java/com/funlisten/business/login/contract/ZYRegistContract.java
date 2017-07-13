package com.funlisten.business.login.contract;

import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;
import com.funlisten.business.login.model.bean.ZYUser;

import java.util.Map;

/**
 * Created by ZY on 17/7/1.
 */

public interface ZYRegistContract {

    interface IView extends ZYIBaseView<IPresenter> {
        void codeSuc();
        void registSuc(ZYUser user);
    }

    interface IPresenter extends ZYIBasePresenter {
        void sendCode(String phone);
        void regUser(Map<String, String> paramas);
    }
}
