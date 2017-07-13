package com.funlisten.business.album.presenter;

import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.business.album.contract.ZYAlbumListHomeContract;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYCategory;

import java.util.List;

/**
 * Created by ZY on 17/6/13.
 */

public class ZYAlbumListHomePresenter extends ZYBasePresenter implements ZYAlbumListHomeContract.IPresenter {

    ZYAlbumListHomeContract.IView mView;

    ZYAlbumModel mModel;

    List<ZYCategory> mCategorys;

    public ZYAlbumListHomePresenter(ZYAlbumListHomeContract.IView view) {
        mView = view;
        mModel = new ZYAlbumModel();
        mView.setPresenter(this);
    }

    public void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getCategorys(), new ZYNetSubscriber<ZYResponse<List<ZYCategory>>>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ZYResponse<List<ZYCategory>> response) {
                super.onSuccess(response);
                mCategorys = response.data;
                mView.refreshCategorys(mCategorys);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }
}
