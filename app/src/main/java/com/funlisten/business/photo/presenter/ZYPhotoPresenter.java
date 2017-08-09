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

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/15.
 */

public class ZYPhotoPresenter extends ZYListDataPresenter<ZYPhotoContract.IView,ZYPhotoModel,ZYPhoto> implements ZYPhotoContract.IPresenter {

    String userId;
    public ZYPhotoPresenter(ZYPhotoContract.IView view, ZYPhotoModel model,String userId) {
        super(view, model);
        this.userId = userId;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.photos(userId,mPageIndex,mRows),new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYPhoto>>>(){
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
    public void upLoadPhoto(File data) {
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.addPhoto(data),new ZYNetSubscriber<ZYResponse<ZYPhoto>>(){

            @Override
            public void onSuccess(ZYResponse<ZYPhoto> response) {
                mView.hideProgress();
//               mDataList.add(response.data);
                loadData();
                mView.showList(true);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                mView.hideProgress();
            }
        }));

    }
    @Override
    public void deletePhoto(final ArrayList<ZYPhoto> list) {
        String ids ="";
        for (ZYPhoto photo:list) ids = photo.id+",";
        mView.showProgress();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.delPhoto(ids),new ZYNetSubscriber<ZYResponse>(){

            @Override
            public void onSuccess(ZYResponse response) {
                mView.hideProgress();
                for (ZYPhoto photo:list) mDataList.remove(photo);
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
