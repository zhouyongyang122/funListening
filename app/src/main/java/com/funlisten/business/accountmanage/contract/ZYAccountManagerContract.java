package com.funlisten.business.accountmanage.contract;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYIBasePresenter;
import com.funlisten.base.mvp.ZYIBaseView;

import rx.Observable;

/**
 * Created by gd on 2017/7/24.
 */

public interface ZYAccountManagerContract {

    interface View<P> extends ZYIBaseView<P> {
        void sendCodeFail();
        void checkCodeSuccces();
    }

    interface Presenter<D> extends ZYIBasePresenter {
        void getCode(String phone,String type);
        void checkCode(String phone,String type, String code);
        void updatePass(String oldPass, String newPass);
        void findPass(String phone,String code,String password);
    }
}
