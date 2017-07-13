package com.funlisten.business.login.presenter;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.event.ZYEventLoginSuc;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.login.model.bean.ZYLoginUser;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.business.login.contract.ZYLoginContract;
import com.funlisten.business.login.model.ZYLoginModel;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.login.model.bean.ZYUser;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ZY on 17/6/30.
 */

public class ZYLoginPresenter extends ZYBasePresenter implements ZYLoginContract.IPresenter {

    ZYLoginContract.IView mView;

    ZYLoginModel mModel;

    public ZYLoginPresenter(ZYLoginContract.IView view) {
        mView = view;
        mView.setPresenter(this);
        mModel = new ZYLoginModel();
    }

    @Override
    public void login(String phone, String pwd) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.login(phone, pwd), new ZYNetSubscriber<ZYResponse<ZYLoginUser>>() {
            @Override
            public void onSuccess(ZYResponse<ZYLoginUser> response) {
                super.onSuccess(response);
                mView.hideProgress();
                ZYLoginUser loginUser = response.data;
                ZYUser user = response.data.userDto;
                user.token = loginUser.token;
                mView.loginSuc(response.data.userDto);
                ZYUserManager.getInstance().setUser(user);
                EventBus.getDefault().post(new ZYEventLoginSuc());
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                mView.hideProgress();
            }
        }));
    }
}
