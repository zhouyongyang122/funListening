package com.funlisten.business.login.presenter;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.business.login.contract.ZYRegistContract;
import com.funlisten.business.login.model.ZYLoginModel;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.utils.ZYToast;

import java.util.Map;

/**
 * Created by ZY on 17/7/1.
 */

public class ZYRegistPresenter extends ZYBasePresenter implements ZYRegistContract.IPresenter {

    ZYRegistContract.IView mView;

    ZYLoginModel mModel;

    public ZYRegistPresenter(ZYRegistContract.IView iView) {
        this.mView = iView;
        mModel = new ZYLoginModel();
        mView.setPresenter(this);
    }

    public void sendCode(String phone) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getCode(phone, ZYLoginModel.TYPE_REGIST_CODE), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
                mView.hideProgress();
                mView.codeSuc();
            }

            @Override
            public void onFail(String message) {
                mView.hideProgress();
//                super.onFail(message);
                mView.codeSuc();
            }
        }));
    }

    public void regUser(Map<String, String> paramas) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.regUser(paramas), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
                mView.hideProgress();
                super.onSuccess(response);
                mView.registSuc(null);
            }

            @Override
            public void onFail(String message) {
                mView.hideProgress();
                super.onFail(message);
            }
        }));
    }

    @Override
    public void checkPhoneIsExists(String phone) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.checkPhoneIsExists(phone), new ZYNetSubscriber<ZYResponse<Boolean>>() {
            @Override
            public void onSuccess(ZYResponse<Boolean> response) {
                mView.hideProgress();
                super.onSuccess(response);
                mView.isShowPwd(!response.data);
            }

            @Override
            public void onFail(String message) {
                mView.hideProgress();
                super.onFail(message);
            }
        }));
    }
}
