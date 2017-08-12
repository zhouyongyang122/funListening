package com.funlisten.business.album.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBaseModel;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.business.album.contract.ZYABatchDownContract;
import com.funlisten.business.album.contract.ZYAlbumDetailContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.album.model.bean.ZYAlbumTitle;
import com.funlisten.business.album.model.bean.ZYComment;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYBatchDownPresenter extends ZYBasePresenter implements ZYABatchDownContract.IPresenter {

    ZYABatchDownContract.IView mView;

    ZYAlbumModel mModel;

    ZYAlbumDetail albumDetail;

    ArrayList<ZYAudio> mDatas = new ArrayList<>();

    String mSortType = ZYAlbumModel.SORT_ASC;

    int totalCount;
    public ZYBatchDownPresenter(ZYABatchDownContract.IView view, ZYAlbumDetail albumDetail,int totalCount) {
        mView = view;
        this.albumDetail = albumDetail;
        mModel = new ZYAlbumModel();
        mView.setPresenter(this);
        this.totalCount = totalCount;
    }

    @Override
    public void subscribe() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getAudios(1, totalCount, albumDetail.id, mSortType), new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYAudio>>>() {

            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYAudio>> response) {
                mDatas.addAll(response.data.data);
                mView.showDatas(mDatas);
            }

            @Override
            public void onFail(String message) {
                mView.showError();
            }
        }));
    }


    public ArrayList<ZYAudio> getDatas() {
        return mDatas;
    }

    @Override
    public ZYAlbumDetail getAlbumDetail() {
        return albumDetail;
    }
}
