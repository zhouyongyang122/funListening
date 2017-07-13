package com.funlisten.business.album.presenter;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.album.contract.ZYAlbumHomeContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumHomePresenter extends ZYBasePresenter implements ZYAlbumHomeContract.IPresenter {

    ZYAlbumHomeContract.IView mView;

    ZYAlbumModel mModel;

    int mAlbumId;

    ZYAlbumDetail mAlbumDetail;

    public ZYAlbumHomePresenter(ZYAlbumHomeContract.IView view,int albumId) {
        mView = view;
        mModel = new ZYAlbumModel();
        mView.setPresenter(this);
        mAlbumId = albumId;
    }

    public void load(){}


    @Override
    public void subscribe() {
        mView.showLoading();
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getAlbumDetail(mAlbumId), new ZYNetSubscriber<ZYResponse<ZYAlbumDetail>>() {

            @Override
            public void onSuccess(ZYResponse<ZYAlbumDetail> response) {
                mView.hideLoading();
                mAlbumDetail = response.data;
                mView.showDetail(mAlbumDetail);
            }

            @Override
            public void onFail(String message) {
                mView.showError();
            }
        }));
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public ZYAlbumDetail getAlbumDetail() {
        return mAlbumDetail;
    }
}
