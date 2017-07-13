package com.funlisten.business.main.presenter;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.business.main.contract.ZYHomeContract;
import com.funlisten.business.main.model.ZYMainModel;
import com.funlisten.business.main.model.bean.ZYHome;

/**
 * Created by ZY on 17/5/19.
 */

public class ZYHomePresenter extends ZYBasePresenter implements ZYHomeContract.IPresenter {

    ZYHomeContract.IView mIView;

    ZYMainModel mModel;

    ZYHome mData;

    public ZYHomePresenter(ZYHomeContract.IView view) {
        mIView = view;
        mModel = new ZYMainModel();
        mIView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mIView.showLoading();
        loadData();
    }

    public void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getHomeData(), new ZYNetSubscriber<ZYResponse<ZYHome>>() {
            @Override
            public void onSuccess(ZYResponse<ZYHome> response) {
                super.onSuccess(response);
                mIView.hideLoading();
                if (response.data != null) {
                    mData = response.data;
                    mIView.refreshView(mData);
                } else {
                    mIView.showError();
                }
            }

            @Override
            public void onFail(String message) {
                mIView.showError();
            }
        }));
    }
}
