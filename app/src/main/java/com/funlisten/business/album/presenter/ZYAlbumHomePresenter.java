package com.funlisten.business.album.presenter;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.album.contract.ZYAlbumHomeContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import rx.Observable;

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
                followState();
                isFavorite();
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

    public void favorite() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.favorite(ZYBaseModel.ALBUM_TYPE, mAlbumDetail.id + ""), new ZYNetSubscriber<ZYResponse<Object>>() {
            @Override
            public void onSuccess(ZYResponse<Object> response) {
                mAlbumDetail.isFavorite = true;
                mView.refreshFavorite(mAlbumDetail);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }


    public void favoriteCancel() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.favoriteCancel(ZYBaseModel.ALBUM_TYPE, mAlbumDetail.id + ""), new ZYNetSubscriber<ZYResponse<Object>>() {
            @Override
            public void onSuccess(ZYResponse<Object> response) {
                mAlbumDetail.isFavorite = false;
                mView.refreshFavorite(mAlbumDetail);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    public void follow() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.follow(mAlbumDetail.publisher.id + ""), new ZYNetSubscriber<ZYResponse<Object>>() {
            @Override
            public void onSuccess(ZYResponse<Object> response) {
                mAlbumDetail.followSate = ZYBaseModel.FOLLOW_HAS_STATE;
                mView.refreshFollow(mAlbumDetail);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    public void followCancle() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.followCancle(mAlbumDetail.publisher.id + ""), new ZYNetSubscriber<ZYResponse<Object>>() {
            @Override
            public void onSuccess(ZYResponse<Object> response) {
                mAlbumDetail.followSate = ZYBaseModel.FOLLOW_NO_STATE;
                mView.refreshFollow(mAlbumDetail);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    public void isOrder(String  objectId){
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.isOrder("album",objectId), new ZYNetSubscriber<ZYResponse<Boolean>>() {
            @Override
            public void onSuccess(ZYResponse<Boolean> response) {
                Boolean show = response.data;
                mView.isShowPay(!show);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
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
