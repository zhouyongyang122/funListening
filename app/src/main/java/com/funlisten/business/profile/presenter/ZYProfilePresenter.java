package com.funlisten.business.profile.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.business.profile.contract.ZYProfileContract;
import com.funlisten.business.profile.model.ZYProfileModel;
import com.funlisten.business.profile.view.viewholder.ZYProfileVH;
import com.funlisten.business.user.model.ZYUserList;
import com.funlisten.service.net.ZYNetManager;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.ZYLog;
import com.funlisten.utils.ZYToast;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func3;

/**
 * Created by Administrator on 2017/7/16.
 */

public class ZYProfilePresenter extends ZYListDataPresenter<ZYProfileContract.IView,ZYProfileModel,ZYAlbumDetail> implements ZYProfileContract.IPresenter{

    ZYListResponse<ZYPhoto> zyPhotoList;
    ZYUser user;
    int  totalCount;
    int categoryId;
    String userId;


    public ZYProfilePresenter(ZYProfileContract.IView view, ZYProfileModel model, int categoryId, String userId) {
        super(view, model);
        this.categoryId = categoryId;
        this.userId = userId;
    }

    @Override
    protected void loadData() {
        Observable<ZYResponse<ZYListResponse<ZYAlbumDetail>>> observable = Observable.zip(
                mModel.getUserInfo(userId), mModel.photos(userId), mModel.getAlbums(mPageIndex, mRows, categoryId, Integer.parseInt(userId)), new Func3<ZYResponse<ZYUser>, ZYResponse<ZYListResponse<ZYPhoto>>, ZYResponse<ZYListResponse<ZYAlbumDetail>>, ZYResponse<ZYListResponse<ZYAlbumDetail>>>() {
                    @Override
                    public ZYResponse<ZYListResponse<ZYAlbumDetail>> call(ZYResponse<ZYUser> zyUserZYResponse, ZYResponse<ZYListResponse<ZYPhoto>> zyListResponseZYResponse, ZYResponse<ZYListResponse<ZYAlbumDetail>> zyListResponseZYResponse2) {
                        user = zyUserZYResponse.data;
                        zyPhotoList = zyListResponseZYResponse.data;
                        return zyListResponseZYResponse2;
                    }
                });

        mSubscriptions.add(ZYNetSubscription.subscription(observable,new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYAlbumDetail>>>(){
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYAlbumDetail>> response) {
                if(response.data.data == null || response.data.data.isEmpty()){
                    List<ZYAlbumDetail> lists = new ArrayList<>();
                    ZYAlbumDetail user1 =   new ZYAlbumDetail();
                    lists.add(user1);
                    ZYListResponse response1 =  new ZYListResponse();
                    response1.data = lists;
                    response.data = response1;
                    ZYProfileVH.totalCount = true;
                }
                totalCount = response.data.totalCount;
                success(response);
                mView.refreshView();
                mView.hideLoading();
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
               fail(message);
            }
        }));
    }

    @Override
    public void follow(String userId) {
        mSubscriptions.add(ZYNetSubscription.subscription(ZYNetManager.shareInstance().getApi().follow(userId),new ZYNetSubscriber<ZYResponse>(){
            @Override
            public void onSuccess(ZYResponse response) {
                mView.showToast("关注成功");
                ZYLog.i(response.data.toString());
            }

            @Override
            public void onFail(String message) {
                ZYLog.d(message);
            }
        }));
    }

    @Override
    public void unFollow(String userId) {
        mSubscriptions.add(ZYNetSubscription.subscription(ZYNetManager.shareInstance().getApi().followCancle(userId),new ZYNetSubscriber<ZYResponse>(){
            @Override
            public void onSuccess(ZYResponse response) {
                ZYLog.i(response.data.toString());
            }

            @Override
            public void onFail(String message) {
                ZYLog.d(message);
            }

        }));
    }

    @Override
    public ZYListResponse<ZYPhoto> getZyPhotoList(){
        return zyPhotoList;
    }

    @Override
    public  ZYUser getUser(){
        return user;
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }
}
