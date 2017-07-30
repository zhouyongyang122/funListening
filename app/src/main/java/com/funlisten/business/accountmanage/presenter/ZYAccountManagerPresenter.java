package com.funlisten.business.accountmanage.presenter;

import com.funlisten.ZYApplication;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.accountmanage.contract.ZYAccountManagerContract;
import com.funlisten.business.accountmanage.contract.ZYModifyPwd;
import com.funlisten.business.accountmanage.model.ZYAccountManageModel;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.ZYToast;

/**
 * Created by gd on 2017/7/24.
 */

public class ZYAccountManagerPresenter extends ZYBasePresenter implements ZYAccountManagerContract.Presenter{

    ZYAccountManageModel mModel;
    ZYAccountManagerContract.View mView;

    ZYModifyPwd modifyPwd;

    public ZYAccountManagerPresenter(ZYAccountManageModel mModel) {
        this.mModel = mModel;
    }

    public ZYAccountManagerPresenter(ZYAccountManageModel model, ZYAccountManagerContract.View mView) {
        this.mModel = model;
        this.mView = mView;
    }

    public void setModifyPwd(ZYModifyPwd modifyPwd){
        this.modifyPwd = modifyPwd;
    }

    @Override
    public void getCode(String phone, String type) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getCode(phone,type),new ZYNetSubscriber<ZYResponse>(){

            @Override
            public void onSuccess(ZYResponse response) {
                super.onSuccess(response);
                ZYToast.show(ZYApplication.getInstance().getCurrentActivity(),"发送成功");
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                mView.sendCodeFail();
            }
        }));
    }

    @Override
    public void checkCode(String phone, String type, String code) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.checkCode(phone,type,code),new ZYNetSubscriber<ZYResponse>(){

            @Override
            public void onSuccess(ZYResponse response) {
                super.onSuccess(response);
                mView.checkCodeSuccces();
                ZYToast.show(ZYApplication.getInstance().getCurrentActivity(),"绑定成功");
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    @Override
    public void updatePass(String oldPass, String newPass) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.updatePass(oldPass,newPass),new ZYNetSubscriber<ZYResponse>(){

            @Override
            public void onSuccess(ZYResponse response) {
                super.onSuccess(response);
                ZYToast.show(ZYApplication.getInstance().getCurrentActivity(),"修改成功");
                modifyPwd.onSuccess();
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                ZYToast.show(ZYApplication.getInstance().getCurrentActivity(),"修改失败");
            }
        }));
    }

    @Override
    public void findPass(String phone, String code, String password) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.findPass(phone,code,password),new ZYNetSubscriber<ZYResponse>(){

            @Override
            public void onSuccess(ZYResponse response) {
                super.onSuccess(response);
                ZYToast.show(ZYApplication.getInstance().getCurrentActivity(),"密码找回成功");
                mView.checkCodeSuccces();
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                ZYToast.show(ZYApplication.getInstance().getCurrentActivity(),"密码找回失败");
            }
        }));
    }
}
