package com.funlisten.business.photo.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.business.photo.contract.ZYPhotoContract;
import com.funlisten.business.photo.model.ZYPhotoModel;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

/**
 * Created by Administrator on 2017/7/15.
 */

public class ZYPhotoPresenter extends ZYListDataPresenter<ZYPhotoContract.IView,ZYPhotoModel,ZYPhoto> implements ZYPhotoContract.IPresenter {

    public ZYPhotoPresenter(ZYPhotoContract.IView view, ZYPhotoModel model) {
        super(view, model);
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.photos(ZYUserManager.getInstance().getUser().userId,mPageIndex,mRows),new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYPhoto>>>(){
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYPhoto>> response) {
               success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    @Override
    public void upLoadPhoto(byte[] data) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.addPhoto(data),new ZYNetSubscriber<ZYResponse<ZYPhoto>>(){

            @Override
            public void onSuccess(ZYResponse<ZYPhoto> response) {
                mView.hideProgress();
               mDataList.add(response.data);
                mView.showList(true);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                mView.hideProgress();
            }
        }));

    }
}
