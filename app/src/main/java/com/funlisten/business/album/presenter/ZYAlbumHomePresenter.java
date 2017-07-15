package com.funlisten.business.album.presenter;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
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

    public ZYAlbumHomePresenter(ZYAlbumHomeContract.IView view, int albumId) {
        mView = view;
        mModel = new ZYAlbumModel();
        mView.setPresenter(this);
        mAlbumId = albumId;
    }

    public void load() {
    }


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

    public void isFavorite() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.isFavorite(ZYBaseModel.ALBUM_TYPE, mAlbumId), new ZYNetSubscriber<ZYResponse<Boolean>>() {
            @Override
            public void onSuccess(ZYResponse<Boolean> response) {
                super.onSuccess(response);
                mAlbumDetail.isFavorite = response.data;
                mView.refreshFavorite(mAlbumDetail);
            }

            @Override
            public void onFail(String message) {
//                super.onFail(message);
            }
        }));
    }

    public void followState() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.isFollowStatus(mAlbumDetail.publisher.id), new ZYNetSubscriber<ZYResponse<String>>() {
            @Override
            public void onSuccess(ZYResponse<String> response) {
                super.onSuccess(response);
                mAlbumDetail.followSate = response.data;
                mView.refreshFollow(mAlbumDetail);
            }

            @Override
            public void onFail(String message) {
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
