package com.funlisten.business.search.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.search.contract.ZYSearchContract;
import com.funlisten.business.search.model.ZYSearchModel;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ZYSearchPresenter extends ZYListDataPresenter<ZYSearchContract.IView,ZYSearchModel,String> implements ZYSearchContract.IPresenter{

    public ZYSearchPresenter(ZYSearchContract.IView view, ZYSearchModel model) {
        super(view, model);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void loadSearch(String type, String keyword) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getSearch(type,keyword),new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYAlbumDetail>>>(){

            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYAlbumDetail>> response) {
                super.onSuccess(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    @Override
    public void loadHotWord() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getSearchWords(),new ZYNetSubscriber<ZYResponse<List<String>>>(){

            @Override
            public void onSuccess(ZYResponse<List<String>> response) {
               mView.showHotWord(response.data);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));

    }
}
