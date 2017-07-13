package com.funlisten.business.main.presenter;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.main.contract.ZYMeContract;
import com.funlisten.business.main.model.ZYMainModel;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

/**
 * Created by ZY on 17/7/3.
 */

public class ZYMePresenter extends ZYBasePresenter implements ZYMeContract.IPresenter {

    ZYMeContract.IView mView;

    ZYMainModel mModel;

    public ZYMePresenter(ZYMeContract.IView view) {
        mModel = new ZYMainModel();
        mView = view;
        mView.setPresenter(this);
    }

    public void getUserInfo() {
        if (!ZYUserManager.getInstance().isGuesterUser(false)) {
            mSubscriptions.add(ZYNetSubscription.subscription(mModel.getUserInfo(ZYUserManager.getInstance().getUser().id), new ZYNetSubscriber<ZYResponse<ZYUser>>() {
                @Override
                public void onSuccess(ZYResponse<ZYUser> response) {
                    super.onSuccess(response);
                    ZYUserManager.getInstance().updateUser(response.data);
                    mView.refreshUserInfo();
                }

                @Override
                public void onFail(String message) {
//                    super.onFail(message);
                }
            }));
        }
    }

}
