package com.funlisten.business.album.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.album.contract.ZYAlbumDetailContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYAlbumTitle;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import rx.Observable;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumDetailPresenter extends ZYBasePresenter implements ZYAlbumDetailContract.IPresenter {

    ZYAlbumDetailContract.IView mView;

    ZYAlbumModel mModel;

    int mAlbumId;

    ArrayList<Object> mDatas = new ArrayList<Object>();

    List<ZYAlbumDetail.Detail> mDetails;

    public ZYAlbumDetailPresenter(ZYAlbumDetailContract.IView view, int albumId) {
        mView = view;
        mAlbumId = albumId;
        mModel = new ZYAlbumModel();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (mDetails != null && mDetails.size() > 0) {
            mDatas.clear();
            loadComments(mDetails);
        }
    }

    @Override
    public void loadComments(List<ZYAlbumDetail.Detail> details) {
        mView.showLoading();
        if (details != null && details.size() > 0) {
            mDetails = details;
            for (ZYAlbumDetail.Detail detail : details) {
                mDatas.add(new ZYAlbumTitle(detail.title));
                mDatas.add(detail);
            }
        }
        loadComments();
    }

    private void loadComments() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getComments(mAlbumId + "", 1, 4), new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYComment>>>() {
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYComment>> response) {
                super.onSuccess(response);
                mDatas.add(new ZYAlbumTitle("评论"));
                List<ZYComment> mComments = response.data.data;
                if (mComments != null && mComments.size() > 0) {
                    mDatas.addAll(mComments);
                }
                mDatas.add(new ZYComment(-1));
                mView.hideLoading();
                mView.showDatas(mDatas);
            }

            @Override
            public void onFail(String message) {
                mView.showError();
            }
        }));
    }

    @Override
    public void suport(ZYComment comment) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.suport(comment.id + "", ZYBaseModel.COMMENT_TYPE), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
                mView.showDatas(mDatas);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    @Override
    public void suportCancle(ZYComment comment) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.suportCanlce(comment.id + "", ZYBaseModel.COMMENT_TYPE), new ZYNetSubscriber<ZYResponse>() {
            @Override
            public void onSuccess(ZYResponse response) {
                mView.showDatas(mDatas);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    @Override
    public int getAlbumId() {
        return mAlbumId;
    }

    public ArrayList<Object> getDatas() {
        return mDatas;
    }



}
