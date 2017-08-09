package com.funlisten.business.myorder.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.favorite.ZYFavorite;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.myorder.contract.ZYMyOrderContract;
import com.funlisten.business.myorder.model.ZYMyOrderModel;
import com.funlisten.business.order.ZYOrder;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.user.model.ZYUserList;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by gd on 2017/7/22.
 */

public class ZYMyOrderPresenter extends ZYListDataPresenter<ZYMyOrderContract.IView,ZYMyOrderModel,ZYFavorite> implements ZYMyOrderContract.IPresenter {

    int  type;
    public ZYMyOrderPresenter(ZYMyOrderContract.IView view, ZYMyOrderModel model, int type) {
        super(view, model);
        this.type = type;
    }

    @Override
    protected void loadData() {
        Observable observable;
        if(type == 1)
         observable = mModel.getFavorites("album",mPageIndex,mRows);
        else observable = mModel.getorders("album",mPageIndex,mRows);

        mSubscriptions.add(ZYNetSubscription.subscription(observable,new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYFavorite>>>(){
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYFavorite>> response) {
                super.onSuccess(response);
//                List<ZYOrder> lists = new ArrayList<>();
//                ZYAudio user1 =   new ZYAudio();
//                user1.costType = "paid";
//                user1.coverUrl  = "http://img4q.duitang.com/uploads/item/201411/05/20141105003804_hQFiB.jpeg";
//                user1.title = "杨幂";
//                user1.playCount = 20;
//
//                ZYAlbumDetail detail = new ZYAlbumDetail();
//                detail.audioCount = 30;
//
//                ZYOrder order = new ZYOrder();
//                order.gmtCreate  ="2017-07-24 12:34";
//                order.audio = user1;
//                order.album = detail;
//
//                lists.add(order);
//
//                ZYListResponse response1 =  new ZYListResponse();
//                response1.data = lists;
//                response.data = response1;
                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));

    }
}
