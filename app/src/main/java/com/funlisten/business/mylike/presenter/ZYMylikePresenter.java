package com.funlisten.business.mylike.presenter;


import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.favorite.ZYFavorite;
import com.funlisten.business.mylike.contract.ZYMyLikeContract;
import com.funlisten.business.mylike.model.ZYMyLikeModel;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYMylikePresenter extends ZYListDataPresenter<ZYMyLikeContract.IView,ZYMyLikeModel,ZYFavorite> implements ZYMyLikeContract.IPresenter{

    public ZYMylikePresenter(ZYMyLikeContract.IView view, ZYMyLikeModel model) {
        super(view, model);
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getFavorites("audio",mPageIndex,mRows), new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYFavorite>>>() {

            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYFavorite>> response) {
                super.onSuccess(response);
                success(response);
                mView.refreshView(response.data.totalCount);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
                fail(message);
            }
        }));
    }

    @Override
    public void getAlbumDetail(final  ZYAudio audio) {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getAlbumDetail(audio.albumId), new ZYNetSubscriber<ZYResponse<ZYAlbumDetail>>() {
            @Override
            public void onSuccess(ZYResponse<ZYAlbumDetail> response) {
                mView.loadAudio(response.data,audio);
                super.onSuccess(response);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }
}
