package com.funlisten.business.album.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.album.contract.ZYAlbumAudiosContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumAudiosPresenter extends ZYListDataPresenter<ZYAlbumAudiosContract.IView, ZYAlbumModel, ZYAudio> implements ZYAlbumAudiosContract.IPresenter {

    int mAlbumId;

    String mSortType = ZYAlbumModel.SORT_ASC;

    public ZYAlbumAudiosPresenter(ZYAlbumAudiosContract.IView view, ZYAlbumModel model, int albumId) {
        super(view, model);
        mAlbumId = albumId;
    }

    @Override
    public void subscribe() {
        mView.showLoading();
        mDataList.clear();
        loadData();
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getAudios(mPageIndex, mRows, mAlbumId, mSortType), new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYAudio>>>() {

            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYAudio>> response) {
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    public void setSortType(String sortType) {
        mSortType = sortType;
    }

    public void choiceEpisode(int start) {
        mPageIndex = (start / mRows) + 1;
        subscribe();
    }

    public void changerSortType() {
        if (mSortType == ZYAlbumModel.SORT_ASC) {
            mSortType = ZYAlbumModel.SORT_DESC;
        } else {
            mSortType = ZYAlbumModel.SORT_ASC;
        }
        subscribe();
    }
}
