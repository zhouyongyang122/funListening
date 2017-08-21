package com.funlisten.business.persondata.presenter;

import com.funlisten.ZYApplication;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.event.ZYEventUpdateUserInfo;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.persondata.contract.ZYPersonContract;
import com.funlisten.business.persondata.model.ZYPersonModel;
import com.funlisten.business.set.model.bean.ZYProvince;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/23.
 */

public class ZYPersonPresenter extends ZYBasePresenter implements ZYPersonContract.IPresenter {
    ZYPersonModel mModel;
    ZYPersonContract.IView mView;
    ArrayList<ZYProvince> zyProvinceArrayList = new ArrayList<>();

    public ZYPersonPresenter(ZYPersonContract.IView iView, ZYPersonModel model) {
        this.mModel = model;
        this.mView = iView;
    }

    @Override
    public void subscribe() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getCities(), new ZYNetSubscriber<ZYResponse<List<ZYProvince>>>() {
            @Override
            public void onSuccess(ZYResponse<List<ZYProvince>> response) {
                super.onSuccess(response);
                zyProvinceArrayList.addAll(response.data);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    @Override
    public void updateUserAvatar(File photo) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.updateUserAvatar(photo), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
                mView.hideProgress();
                EventBus.getDefault().post(new ZYEventUpdateUserInfo());
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                mView.hideProgress();
            }
        }));
    }

    @Override
    public void updateUserDetail(Map<String, String> paramas) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.updateUserDetail(paramas), new ZYNetSubscriber<ZYResponse>() {

            @Override
            public void onSuccess(ZYResponse response) {
                super.onSuccess(response);
                mView.updateUser();
                EventBus.getDefault().post(new ZYEventUpdateUserInfo());
                ZYToast.show(ZYApplication.getInstance().getCurrentActivity(), "修改成功");
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));

    }

    public ArrayList<ZYProvince> getProvince() {
        return zyProvinceArrayList;
    }
}
